package com.soft.api.service.his.shengguke;

//import com.codingapi.tx.annotation.TxTransaction;
import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.soft.api.feign.LISServiceFeign;
import com.soft.api.mapper.his.shengguke.IEMRDocumentRegistryMapper;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName EMRDocumentRegistryImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/11/24
 * @Version V1.0
 **/
@Service
public class EMRDocumentRegistryImpl {

    private final Logger logger = LoggerFactory.getLogger(EMRDocumentRegistryImpl.class);

    @Autowired
    private IEMRDocumentRegistryMapper emrDocumentRegistryMapper;

    @Autowired
    private QueryHISKeyCommon queryHISKeyCommon;

    @Autowired
    private LISServiceFeign lisServiceFeign;

//    @LcnTransaction() //分布式事务
    @Transactional(rollbackFor=Exception.class)
//    @TxTransaction(isStart = true)
    public ResponseEntity registry(Map<String,Object> paramMap) throws Exception {
            if (paramMap == null || paramMap.isEmpty()) {
                return ResultMessage.error(417,"输入参数错误");
            }
            Map map = (Map) paramMap.get("MsgHeader");
            String msgType = (String) map.get("MsgType");
//            return testlcn();
            switch (msgType){
                case "CDR_5411": //检查报告上传（放射）
                    return emrDocumentRegistryPUE(paramMap, "Report_PACS");
                case "CDR_5412": //检查报告上传（超声）
                    return emrDocumentRegistryPUE(paramMap, "Report_US");
                case "CDR_5413":  //检查报告上传（内镜）
                    return emrDocumentRegistryPUE(paramMap, "Report_ES");
                case "CDR_5414":  //检查报告上传（病理）
                    return emrDocumentRegistryPUE(paramMap, "Report_PE");
                case "CDR_5415":  //检查报告上传（心电）
                    return EMRDocumentRegistryEcg(paramMap);
                case "CDR_5401":  //检验报告上传
                    return lisServiceFeign.emrDocumentRegistryLIS(JSON.toJSONString(paramMap));
                default:
                    return ResultMessage.error(417,"无法判断MsgType【"+msgType+"】");
            }
    }

    @Transactional(rollbackFor=Exception.class)
    @LcnTransaction(propagation = DTXPropagation.REQUIRED)
    public ResponseEntity testlcn(){
        Map map = new HashMap();
        map.put("id","1");
        map.put("name","hello");
        emrDocumentRegistryMapper.test(map);
        lisServiceFeign.testTxlcn("");
        return ResultMessage.success(200);
    }


    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity emrDocumentRegistryPUE(Map map, String nodeName) throws Exception {
        // 设置 ELECTRONICREQUESTNOTEID 值
        Map nodeNames = (Map) map.get(nodeName);
        map.put("ELECTRONICREQUESTNOTEID", nodeNames.get("ElectronicRequestNoteId"));
        map.put("ExaminationItemCode", nodeNames.get("ExaminationItemCode"));
        //查询检查报表是否已审核。 已审核则不做任何操作
        int isReviewResult = emrDocumentRegistryMapper.isReview(map);
        if(isReviewResult < 1){
            Map hashMap = emrDocumentRegistryMapper.queryDocumentRegistryPUEPara(map);
            if(null == hashMap){
                return ResultMessage.error(500,"没有查询到相关检查申请记录");
            }
            long jlxh = queryHISKeyCommon.getMaxANDValide("EMR_JCBG",1);
            hashMap.put("JLXH", jlxh);
            Map patient = (Map) map.get("Patient");
            hashMap.put("BRBH", patient.get("SourcePatientId"));
            hashMap.put("LCZD", nodeNames.get("ExaminationResult"));
            hashMap.put("BGKS", nodeNames.get("ReportDeptName"));
            hashMap.put("BGYS", nodeNames.get("ReportDoctor"));
            String reportDateTime = (String) nodeNames.get("ReportDateTime");
            reportDateTime = reportDateTime.replace("T" ," ");
            hashMap.put("BGRQ", reportDateTime.split("\\.")[0]);
            hashMap.put("KDRQ" , hashMap.get("KDRQ").toString().split("\\.")[0]);
            hashMap.put("JCJG", nodeNames.get("ExaminationDisplay"));
            //获取医嘱序号用于检查报告回写,分别保存多个报告,用医嘱序号区分
            Map yzxhMap = emrDocumentRegistryMapper.getJcxmYzxh(map);
            if(yzxhMap == null || yzxhMap.size() == 0){
                throw new Exception("未查询到【JCXM】医嘱序号数据,查询参数【"+map+"】");
            }
            map.put("YZXH", yzxhMap.get("YZXH"));
            hashMap.put("YZXH", yzxhMap.get("YZXH"));
            //删除之前的报告，添加新报告
            emrDocumentRegistryMapper.deleteBySqdh(map);
            emrDocumentRegistryMapper.updateJCSQ(map);
            int rowInt = emrDocumentRegistryMapper.addDocumentRegistryPUE(hashMap);
            if(rowInt < 1){
                throw new Exception("检查报告【"+nodeName+"】上传失败,数据【0】条");
            }
            return ResultMessage.success(200,"检查报告【"+nodeName+"】上传成功");
        }else{
            return ResultMessage.error(500,"检查报告【"+nodeName+"】已审核,不能再保存");
        }
    }

    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity EMRDocumentRegistryEcg(Map map) throws Exception{
        int addRows = 0;
        // 设置 ELECTRONICREQUESTNOTEID 值
        Map report_ECG = (Map) map.get("Report_ECG");
        map.put("ELECTRONICREQUESTNOTEID", report_ECG.get("ElectronicRequestNoteId"));
//        map.put("ExaminationItemCode", report_ECG.get("ExaminationItemCode"));
        int count = emrDocumentRegistryMapper.isReview(map);
        if(count < 1){
            Map hashMap = emrDocumentRegistryMapper.queryDocumentRegistryPUEPara(map);
            if(null == hashMap){
                return ResultMessage.error(500,"没有查询到相关检查申请记录");
            }
            long jlxh = queryHISKeyCommon.getMaxANDValide("EMR_JCBG",1);
            hashMap.put("JLXH", jlxh);
            Map patient = (Map) map.get("Patient");
            hashMap.put("BRBH", patient.get("SourcePatientId"));
            hashMap.put("LCZD", report_ECG.get("ExaminationResult"));
            hashMap.put("BGKS", report_ECG.get("ReportDeptName"));
            hashMap.put("BGYS", report_ECG.get("ReportDoctor"));
            String reportDateTime = (String) report_ECG.get("ReportDateTime");
            reportDateTime = reportDateTime.replace("T" ," ");
            hashMap.put("BGRQ", reportDateTime.split("\\.")[0]);
            hashMap.put("KDRQ" , hashMap.get("KDRQ").toString().split("\\.")[0]);
            hashMap.put("JCJG", this.getJcjg(report_ECG));
//            Map yzxhMap = emrDocumentRegistryMapper.getJcxmYzxh(map);
//            if(yzxhMap == null || yzxhMap.size() == 0){
//                throw new Exception("未查询到【JCXM】医嘱序号数据,查询参数【"+map+"】");
//            }
//            map.put("YZXH", yzxhMap.get("YZXH"));
//            hashMap.put("YZXH", yzxhMap.get("YZXH"));
            //删除之前存在的报告
            emrDocumentRegistryMapper.deleteBySqdh(map);
            emrDocumentRegistryMapper.updateJCSQ(map);
            addRows = emrDocumentRegistryMapper.addDocumentRegistryPUE(hashMap);
            if(addRows < 1){
                throw new Exception("（心电）检查报告【Report_ECG】上传失败,数据【0】条");
            }
            return ResultMessage.success(200,"（心电）检查报告【Report_ECG】上传成功");
        }else{
            return ResultMessage.error(500,"（心电）检查报告【Report_ECG】已审核");
        }
    }

    @SuppressWarnings("rawtypes")
    public String getJcjg(Map report_ECG){
        StringBuffer sb = new StringBuffer();
        sb.append("心率:");
        sb.append(report_ECG.get("HeartRate"));
        sb.append("（次/min）");
        sb.append("\n");
        sb.append("QT间期:");
        sb.append(report_ECG.get("ECG_QT"));
        sb.append("（ms）");
        sb.append("\n");
        sb.append("P波:");
        sb.append(report_ECG.get("ECG_P"));
        sb.append("\n");
        sb.append("QRS波:");
        sb.append(report_ECG.get("ECG_QRS"));
        sb.append("（ms）");
        sb.append("\n");
        sb.append("P-R间期:");
        sb.append(report_ECG.get("ECG_PR"));
        sb.append("（ms）");
        sb.append("\n");
        sb.append("QTC:");
        sb.append(report_ECG.get("ECG_QTc"));
        sb.append("（ms）");
        sb.append("\n");
        sb.append("QRS轴:");
        sb.append(report_ECG.get("ECG_QRS_Axis"));
        sb.append("\n");
        sb.append("RV5/6:");
        sb.append(report_ECG.get("ECG_RV5"));
        sb.append("（mV）");
        sb.append("\n");
        sb.append("SV1:");
        sb.append(report_ECG.get("ECG_SV1"));
        sb.append("（mV）");
        sb.append("\n");
        sb.append(report_ECG.get("ECGFeature"));
        return sb.toString();
    }


    public static String strToDateFormat1(String date, String delPara, String timeFormat, String newTimeFormat) throws Exception {
        StringBuffer date1 = new StringBuffer(date.split("\\.")[0]);
        if(!StringUtils.isEmpty(delPara)){
            int index = date1.indexOf(delPara);
            if(index != -1){
                date1.deleteCharAt(index);
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat(timeFormat);
        formatter.setLenient(false);
        Date newDate = formatter.parse(date1.toString());
        formatter = new SimpleDateFormat(newTimeFormat);
        return formatter.format(newDate);
    }


}
