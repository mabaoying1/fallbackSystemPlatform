package com.soft.api.service.lis.shengguke;

import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.soft.api.feign.HISServiceFeign;
import com.soft.api.mapper.shengguke.IInspectionRelationMapper;
import ctd.net.rpc.util.LabBarcodeUtil;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName LabBarcodeCreateCancelImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/11
 * @Version V1.0
 **/
@Service
public class LabBarcodeCreateCancelImpl {

    @Autowired
    private IInspectionRelationMapper inspectionRelationMapper;

    @Autowired
    private HISServiceFeign hisServiceFeign;

    @Autowired
    private LabBarcodeUtil labBarcodeUtil;

    /**
     * 1、判断门诊/住院 <br>
     * 2、门诊 <br>
     *      取消ms_yj01,ZXRQ,ZXPB,ZXYS <br>
     * 3、住院 <br>
     *     .判断是否出院，出院不允许取消打印条码
     *     .ZY_FYMX 新增负记录数据
     *     .ZY_FYMX 更新FYSL=YTSL
     *     .zy_bqyz 更新sybz = 0, lsbz = 0, qrsj = Null
     * 4、l_lis_sqdmx 更新feestatus = 1,zxpb = 0
     * 5、l_lis_sqd 更新sqdstatus
     * 取消检验条码打印通知
     * @param map
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction(propagation = DTXPropagation.REQUIRED)
    public ResponseEntity labBarcodeCreateCancel(Map<String,Object> map) throws Exception {
        StringBuffer doctadviseno = new StringBuffer("DSF");
        Map barCodeMap = (Map)map.get("LabBarcode");
        doctadviseno.append((String)barCodeMap.get("BarcodeNo"));
        doctadviseno.append("A");
        map.put("DOCTADVISENO", doctadviseno.toString());
        map.put("doctadviseno", doctadviseno.toString());
        Map<String,Object> map1 = new HashMap<>();
        // lis库 sql 查询是否有数据可取消计费
        Map<String, Object> mzZyCodeMap = inspectionRelationMapper.queryCancelMzZyCode(map);
        if(mzZyCodeMap == null){
            return ResultMessage.error(500,"取消检验条码打印通知失败,未查询到数据【"+map+"】");
        }
        Integer mzZyCode = Integer.parseInt(mzZyCodeMap.get("MZZYCODE").toString());
        List<Map<String, Object>> listMap = (List<Map<String, Object>>) inspectionRelationMapper.queryIsRefundData(map);
        List<HashMap<String, Object>> requestList = labBarcodeUtil.getRequstid(listMap);
        map.put("LabRequest",requestList);
        map1.putAll(map);
        if(mzZyCode.intValue() == 1){ //门诊
            //把退费数据参数，传给HIS;  用申请单ID 和 YJXH 去更新HIS
            ResponseEntity responseEntity =  hisServiceFeign.barcodeCancelYJ01(JSON.toJSONString(listMap));
            if(responseEntity.getCode() == 400){
                throw new Exception("HIS访问超时，作废HIS的MSYJ01失败【方法：barcodeCancelYJ01】;此次访问失败");
            }
//            int count = inspectionRelationMapper.getJytmxxCount(map);
//            if(count == 1){ //当取消条码是申请单最后一个条码时，再改MS_YJ01
//                ResponseEntity responseEntity = hisServiceFeign.barcodeCancelYJ01(JSON.toJSONString(requestList));
//                if(responseEntity.getCode() == 400){
//                    throw new Exception("HIS访问超时，服务熔断【barcodeCancelYJ01】;此次访问失败");
//                }
//            }
            //取消lis 状态 sqdmx
            for(Map<String, Object> mp : listMap){
                int rowsCancelBarCodeBillMark = inspectionRelationMapper.cancelOVBarCodeBillingMarkNew(mp);
                if(rowsCancelBarCodeBillMark < 1 ) throw new Exception("取消条码打印门诊,更新【l_lis_sqdmx】标志失败！");
            }
            //取消lis状态 sqd
            for (HashMap<String,Object> request: requestList) {
                int rInt = inspectionRelationMapper.getSQDMXOVCount(request);
                if(rInt == 0){ //表示sqdmx已经全部取消执行，那么都需要更新SQD状态
                    int updateRows1 = inspectionRelationMapper.updateSqdstatusCance(request);
                    if(updateRows1 < 1) throw new Exception("取消条码打印门诊,更新【l_lis_sqd】标志失败！");
                }
            }
            //更新l_jytmxx_mx
            int updateRows2 = inspectionRelationMapper.updateBarCodeBillingMarkCancel(map);
            if(updateRows2 < 1 ) throw new Exception("取消条码打印门诊,更新【l_jytmxx_mx】标志失败！");
            //更新l_jytmxx
            int rel = inspectionRelationMapper.updateJYTMXXSampleStatus(map);
            if(rel <1) throw new Exception("取消条码打印门诊,更新条码状态");
            //现在取消条码是不删除条码
//            int rel = inspectionRelationMapper.deleteJYTMXX(map);
//            if(rel <1) throw new Exception("取消条码打印门诊,删除【JYTMXX】失败！");
//            rel = inspectionRelationMapper.deleteJYTMXXMX(map);
//            if(rel <1) throw new Exception("取消条码打印门诊,删除【JYTMXXMX】失败！");
        } else if(mzZyCode.intValue() == 2){ //住院
//            ResponseEntity responseEntity = hisServiceFeign.queryCYPB(JSON.toJSONString(mzZyCodeMap));
//            if(responseEntity.getCode() == 400){
//                throw new Exception("HIS访问超时，服务熔断【queryCYPB】;此次访问失败");
//            }
//            if(Integer.parseInt(responseEntity.getResult().toString()) > 0){
//                return ResultMessage.error(500,"取消检验条码打印通知失败,已经出院无法取消条码打印");
//            }
//            //调用取消检验计费接口
//            if(listMap != null && listMap.size() > 0){
//                responseEntity = hisServiceFeign.cancelLISPay(JSON.toJSONString(listMap));
//                if(responseEntity.getCode() == 400){
//                    throw new Exception("HIS访问超时，服务熔断【cancelLISPay】;此次访问失败");
//                }
//            }
//            int rel = inspectionRelationMapper.deleteJYTMXX(map);
//            if(rel <1) throw new Exception("取消条码打印门诊,删除【JYTMXX】失败！");
//            rel = inspectionRelationMapper.deleteJYTMXXMX(map);
//            if(rel <1) throw new Exception("取消条码打印门诊,删除【JYTMXXMX】失败！");
            int rel = inspectionRelationMapper.updateJYTMXXSampleStatus(map);
            if(rel < 1) throw new Exception("取消条码打印住院,更新jytmxx条码状态失败");
            //通过条码号找到对应的申请单(1..N)
            List<Map> reqList = inspectionRelationMapper.getReqByBarCode(map);
            for(Map m : reqList){
                //l_jytmxx根据申请单号和SAMPLESTATUS = 3，count出总条数
                int sInt = inspectionRelationMapper.getBarCodeByStatus(m);
                //l_jytmxx根据申请单号，count出总条数
                int rInt = inspectionRelationMapper.getBarCodeByReqStatus(m);
                if(sInt == rInt){ //如果取消的条数等于申请单下条码总数，说明该申请单下所有条码已取消
                    m.put("REQUESTID", m.get("DOCTREQUESTNO"));
                    inspectionRelationMapper.updateSqdstatusCance(m);
                }
            }
        } else{
            return ResultMessage.error(500,"取消检验条码打印通知失败,未能区分门诊/住院");
        }
        return ResultMessage.success(200,"取消检验条码打印通知成功");
    }

}
