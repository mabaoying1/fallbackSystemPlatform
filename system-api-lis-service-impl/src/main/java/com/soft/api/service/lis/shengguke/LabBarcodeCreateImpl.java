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
import java.util.*;

/**
 * 检验相关
 * @ClassName InspectionRelationImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/2
 * @Version V1.0
 **/
@Service
public class LabBarcodeCreateImpl {

    @Autowired
    private IInspectionRelationMapper inspectionRelationMapper;

    @Autowired
    private QueryLISKeyCommon queryLISKeyCommon;

    @Autowired
    private HISServiceFeign hisServiceFeign;

    @Autowired
    private LabBarcodeUtil labBarcodeUtil;


    /**
     * 检验条码打印通知 </br>
     * 1、插入lis库 l_jytmxx表 <br>
     * 2、判断门诊/出院
     * 3、门诊：更新his库, ms_yj01
     * 4、住院：
     * (1) 查询lis库l_lis_sqd、l_lis_sqdmx
     * (2) 新增住院费用明细 ZY_FYMX
     * 5、更新检验申请单状态
     * 6、更新检验条码明细状态
     * @param map
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    @LcnTransaction(propagation = DTXPropagation.REQUIRED)
    public ResponseEntity labBarcodeCreate(Map<String,Object> map) throws Exception {
        Map labBarcode = (Map) map.get("LabBarcode");
        Object labRequest  =  labBarcode.get("LabRequest");
        List<HashMap<String,Object>> requestList = new ArrayList<HashMap<String,Object>>();
        if (labRequest instanceof  Map){
            HashMap<String,Object> newMap = (HashMap<String,Object>) labRequest;
            requestList.add(newMap);
        }else if (labRequest instanceof List){
            requestList = (ArrayList<HashMap<String,Object>>)labRequest;
        }
        labBarcode.put("LabRequest",requestList);
        map.put("LabBarcode",labBarcode);
        // 得到item下  节点下所有的ItemCode值 再放到map中去
        Object itemcodeText = labBarcodeUtil.getAllItemCode(map);
        map.put("ITEMCODE_TEXT", itemcodeText);
        map.put("RequestList" ,getRequestIDAndCode(requestList));
        long tmxh = queryLISKeyCommon.getMaxANDValide("l_jytmxx_tmxh",1);
        //新增条码信息
        ResponseEntity ylxhRep = addLabBarcodeCreate(map, labBarcode, tmxh);
        if(ylxhRep.getCode() == 300){
            return ResultMessage.success(200, ylxhRep.getMsg());
        }
        ResponseEntity responseEntity = barcodeCreate(map, requestList);
        return responseEntity;
    }

    /**
     * 住院计费/门诊改状态
     * @param map
     * @param requestList
     * @return
     * @throws Exception
     */
    @LcnTransaction(propagation = DTXPropagation.REQUIRED)
    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity barcodeCreate(Map map , List<HashMap<String,Object>> requestList ) throws Exception {
        Integer mzZyCode = inspectionRelationMapper.queryMzZyCode(map);
        if(mzZyCode == null){
            throw new Exception("检验条码打印通知失败,未查询到数据【"+map+"】");
        }
        if(mzZyCode.intValue() == 1 || mzZyCode.intValue() == 3){ //1门诊  3 体检
            for (Map<String,Object> request : requestList){
                request.put("ITEMCODE_TEXT", labBarcodeUtil.getItemCode(request));
            }
            ResponseEntity responseEntity = hisServiceFeign.updateMzZxbz(JSON.toJSONString(map));
            if(responseEntity.getCode() == 400){
                throw new Exception("HIS访问超时，服务熔断【updateMzZxbz】;此次访问失败");
            }
            Map labBarcode = (Map) map.get("LabBarcode");
            for (HashMap<String,Object> request: requestList) {
                request.put("doctadviseno", "DSF"+labBarcode.get("BarcodeNo").toString() +"A");
                //添加明细完成后需更新状态
                int updateRows = inspectionRelationMapper.updateApplicationBillingMark(request);
                if(updateRows < 1){
                    throw new Exception("更新【l_lis_sqdmx】收费状态、执行判别失败");
                }
                //添加明细完成后需更新状态
                updateRows = inspectionRelationMapper.updateBarCodeBillingMark(request);
                if(updateRows < 1){
                    throw new Exception("更新【l_jytmxx_mx】收费状态失败");
                }
            }
        }else if(mzZyCode.intValue() == 2){
            //此处代码因为SGK,lis 要求住院条码打印不计费,改在其他地方计费
//            String gh = labBarcodeUtil.getExeDoctor(map); //员工工号
//            for (Map<String,Object> request : requestList){
//                String requestId = (String)request.get("RequestId");
//                request.put("ITEMCODE_TEXT", labBarcodeUtil.getItemCode(request));
//                List<Map<String, Object>> hashMap = inspectionRelationMapper.queryLabRequestPayBillZY(request);
//                if(hashMap != null && hashMap.size() > 0){
//                    for (int i = 0; i < hashMap.size(); i++) {
//                        Map<String,Object> mappara = hashMap.get(i);
//                        mappara.put("QRGH", gh);
//                        mappara.put("SRGH", gh);
//                        mappara.put("REQUESTID",requestId);
//                        mappara.put("FYRQ", mappara.get("FYRQ").toString().split("\\.")[0]);
//                        mappara.put("JFRQ", mappara.get("JFRQ").toString().split("\\.")[0]);
//                    }
//                    String json = JSON.toJSONString(hashMap);
//                    ResponseEntity responseEntity = hisServiceFeign.lisPay(json);
//                    if(responseEntity.getCode() == 400){
//                        throw new Exception("HIS访问超时，服务熔断【lisPay】;此次访问失败");
//                    }
//                }else{
//                    throw new Exception("查询出检验计费添加的参数失败");
//                }
//            }
        }else{
            throw new Exception("检验条码打印通知失败,未能区分门诊/住院");
        }
        return ResultMessage.success(200,"检验条码打印通知成功");
    }

    /**
     * 新增检验条码信息
     * 1、l_jytmxx
     * 2、l_jytmxx_mx
     * 3、l_lis_sqd
     * 4、住院更新l_lis_sqdmx
     * @param map
     * @return
     */
    @LcnTransaction( propagation = DTXPropagation.REQUIRED)
    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity addLabBarcodeCreate(Map map , Map labBarcode , long tmxh) throws Exception {
        //1、查询l_lis_sqd ,查询出申请单信息
        Map hashMap = (Map) inspectionRelationMapper.queryLabBarcodeCreateParam(map);
        if(hashMap == null){
            throw new Exception("根据【RequestId】在申请单中未找到相关数据");
        }
        map.put("DOCTADVISENO", "DSF" + (String)labBarcode.get("BarcodeNo") + "A");
        Map mzzyCode = inspectionRelationMapper.queryCancelMzZyCode(map);
        if(mzzyCode != null && mzzyCode.size() > 0){
            return ResultMessage.success(300,"【BarcodeNo】存在条码的数据【l_jytmxx】;入参【"+map+"】");
//            throw new Exception("【BarcodeNo】存在条码的数据【l_jytmxx】,请重新处理入参数据;入参【"+map+"】");
        }
        //有可能一个申请单多个条码， 所以不能用这个方式验证
//        int relInt = inspectionRelationMapper.getSQDExcStatus(map);
//        if(relInt > 0){
//            throw new Exception("【RequestId】存在已打条码的数据,请重新处理入参数据;入参【"+map+"】");
//        }

        int rows1 = 0;
        if(hashMap != null){
            //2、获取DQZ
//            long tmxh = queryLISKeyCommon.getMaxANDValide("l_jytmxx_tmxh",1);
            if(tmxh > 0){
                hashMap.put("TMXH",tmxh);
                // 获取条形码BarcodeNo
                hashMap.put("DOCTADVISENO", "DSF" + (String)labBarcode.get("BarcodeNo") + "A");
                List<String> sbList = labBarcodeUtil.getSbList(labBarcode);
                hashMap.put("EXAMINAIM",sbList.get(0));
                hashMap.put("EXAMINAIMCODE",sbList.get(1));
                Double fee = 0d;
                //3、计算费用
                fee = inspectionRelationMapper.getFee(map);
                if(fee == null){
                    throw new Exception("【l_lis_sqdmx】未查询出费用信息");
                }
                hashMap.put("FEE",fee);
                String exeDateTime = (String) labBarcode.get("ExeDateTime");
//                hashMap.put("PRINTTIME",DateUtil.strToDateFormat(exeDateTime, "T"));
                hashMap.put("PRINTTIME",  exeDateTime.replaceAll("T"," "));
                Map exeDoctor = (Map) labBarcode.get("ExeDoctor");
                hashMap.put("PRINTER",(String)exeDoctor.get("#text"));
                hashMap.put("SAMPLENO",(String)labBarcode.get("#BarcodeNo"));
                //4、新增l_jytmxx
                rows1 = inspectionRelationMapper.addLabBarcodeCreate(hashMap);
                if(rows1 < 0){
                    throw new Exception("添加【l_jytmxx】条码主信息失败！");
                }
                /**
                 * 开始添加明细
                 */
                //5、 查询l_lis_sqdmx 添加明细所需要的参数
//                List<Map<String,Object>> hashMapD = (List<Map<String,Object>>) inspectionRelationMapper
//                        .queryLabBarcodeCreateDParam(map);
                //废弃上个查询，是因为多个申请单可能打印一个条码号
                List<Map<String,Object>> hashMapD = (List<Map<String,Object>>) inspectionRelationMapper
                        .queryLabBarcodeCreateDParamNew(map);
                StringBuffer sb = null;
                //6、判断门诊/住院
                Integer mzZyCode = inspectionRelationMapper.queryMzZyCode(map);
                if(mzZyCode == null) throw new Exception("【RequestID】未找到相关数据,不能区分门诊/住院");
                if(hashMapD != null && hashMapD.size() > 0 ){
                    for (int i = 0; i < hashMapD.size(); i++) {
//                        ylxhMap.put( hashMapD.get(i).get("PREHYID") , hashMapD.get(i).get("YLXH"));
                        sb = new StringBuffer("DSF");
                        sb.append((String)labBarcode.get("BarcodeNo"));
                        sb.append("A");
                        ResponseEntity responseEntity = null;
                        //7、如果住院都需要去获取YZXH
                        //现在不需要了，因为拿的sqdmx表JLXH
                        if(mzZyCode == 2){
                            responseEntity = hisServiceFeign.queryYZXHStr(
                                    JSON.toJSONString(hashMapD.get(i))
//                                    hashMapD.get(i).get("YSYZBH").toString(),
//                                    hashMapD.get(i).get("REQUESTID").toString(),
//                                    hashMapD.get(i).get("YLXH").toString()
                            );
                            if(responseEntity.getCode() == 400){
                                throw new Exception("【HIS访问超时，服务熔断;】,queryYZXH获取值失败！");
                            }else if(responseEntity.getCode() == 200){
                                hashMapD.get(i).put("JLXH", responseEntity.getResult());
                            }else{
                                throw new Exception("【ZY_BQYZ】医嘱可能已作废,不能获取JLXH;请核对数据【"+hashMapD.get(i)+"】");
                            }
                        }
                        hashMapD.get(i).put("DOCTADVISENO", sb.toString());
                        hashMapD.get(i).put("TMXH",tmxh);
                        //8、新增l_jytmxx_mx
                        int num = inspectionRelationMapper.addLabBarcodeCreateD(hashMapD.get(i));
                        if(num < 1){
                            throw new Exception("添加【l_jytmxx_mx】条码明细失败！");
                        }
                        //9、如果是住院，更新l_lis_sqdmx中JLXH
                        if(mzZyCode == 2){
                            int num1 = inspectionRelationMapper.updateJLXH1(hashMapD.get(i));
                            if(num1 < 1){
                                throw new Exception("更新【l_lis_sqdmx】jlxh失败！");
                            }
                        }
                    }
                }
            }else{
                throw new Exception("未获取到【l_jytmxx_tmxh】DQZ！");
            }
            //10、更新l_lis_sqd状态 状态改为4 执行条码生成
            //申请单状态　-1:新增　 0: 　作废　1: 暂存　2:提交　3:已收费 4:执行生成条码 99:未收费　
            int rows2 = inspectionRelationMapper.updateSqdstatus(map);
            if(rows2 < 1){
                throw new Exception("更新【l_lis_sqd】Sqdstatus失败！");
            }
        }
        return ResultMessage.success(200,"");
    }

    /**
     * 把requestid 和 itemcode 处理成平级
     * @param requestList
     * @return
     */
    public List<Map> getRequestIDAndCode(List<HashMap<String,Object>> requestList){
        List<Map> tempList = new ArrayList<>();
        Map tempMap = null;
        for(HashMap requestMap : requestList){
            tempMap = new HashMap();
            tempMap.put("RequestId" , requestMap.get("RequestId"));
            if(requestMap.get("Item") instanceof List){
                List<HashMap> item  = (List<HashMap>) requestMap.get("Item");
                for (int i = 0; i < item.size(); i++) {
                    if(item.get(i).get("ItemCode") instanceof Map){
                        Map map1 = (Map) item.get(i).get("ItemCode");
                        tempMap.put("ItemCode" , (String)map1.get("#text"));
                    }else{
                        tempMap.put("ItemCode" , item.get(i).get("ItemCode").toString());
                    }
                }
            }else{
                HashMap item  = (HashMap)requestMap.get("Item");
                if(item.get("ItemCode") instanceof Map){
                    Map map1 = (Map) item.get("ItemCode");
                    tempMap.put("ItemCode" ,(String)map1.get("#text"));
                }else{
                    tempMap.put("ItemCode" ,item.get("ItemCode").toString());
                }
            }
            tempList.add(tempMap);
        }
        return tempList;
    }

}
