package com.soft.api.service.his.shengguke;

import com.alibaba.fastjson.JSONArray;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.soft.api.mapper.his.shengguke.ILisOperateRelateMapper;
import com.soft.api.service.shengguke.ILisOperateRelate;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName LisOperateRelateImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/3
 * @Version V1.0
 **/
@Slf4j
@RestController
public class LisOperateRelateImpl implements ILisOperateRelate{

    @Autowired
    private ILisOperateRelateMapper lisOperateRelateMapper;

    @Autowired
    private QueryHISKeyCommon queryHISKeyCommon;


    @Override
    @Transactional(rollbackFor=Exception.class)
    @RequestMapping(value = "/queryHISSEQ")
    public ResponseEntity queryHISSEQ(String paramJson) {
        Map<String,Object> map = (Map<String,Object>) JSONArray.parse(paramJson);
        long seq = queryHISKeyCommon.getMaxANDValide(map.get("bmc").toString() ,
                Integer.parseInt(map.get("size").toString()));
        return ResultMessage.success(200,"",seq);
    }

    /**
     * 取消条码打印门诊
     * @param paramJson
     * @return
     */
    @Override
    @Transactional(rollbackFor=Exception.class)
    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    @RequestMapping(value = "/barcodeCancelYJ01")
    public ResponseEntity barcodeCancelYJ01(String paramJson) {
        List<Map<String,Object>> listMap = (List<Map<String,Object>>) JSONArray.parse(paramJson);
        for(Map<String,Object> map : listMap){
            //更新MSYJ01
            lisOperateRelateMapper.cancelMSYJExecutionSign(map);
        }
        return ResultMessage.success(200,"");
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    @RequestMapping(value = "/queryCYPB")
    public ResponseEntity queryCYPB(String paramJson){
        Map<String,Object> map = (Map<String,Object>) JSONArray.parse(paramJson);
        return ResultMessage.success(200,"", lisOperateRelateMapper.queryCYPB(map));
    }

    /**
     * lis取消条码打印取消计费
     * @param paramJson
     * @return
     * @throws Exception
     */
    @Override
    @RequestMapping(value = "/cancelLISPay")
    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity cancelLISPay(String paramJson) throws Exception {
        List<Map<String,Object>> list = (List<Map<String,Object>>) JSONArray.parse(paramJson);
        long rowsJlxh = queryHISKeyCommon.getMaxANDValide("ZY_FYMX" , list.size());
        return hisReturnCharge(list , rowsJlxh);
    }

    @Transactional(rollbackFor=Exception.class)
    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    public ResponseEntity hisReturnCharge(List<Map<String,Object>> list , long DQZ) throws Exception {
        int i = 0;
        for (Map<String, Object> hashMap : list) {
            // 查询出退费数据参数
            List<Map<String, Object>> mapParams = lisOperateRelateMapper.queryAddPara(hashMap);
            if(mapParams == null) {
                continue;
            }
            // 更新记录序号
//            long rowsJlxh = queryHISKeyCommon.getMaxANDValide("ZY_FYMX" , 1);
            //查询出更新后的记录序号 再加入map参数中
            for(Map<String, Object> mapParam : mapParams){
                mapParam.put("JLXH", DQZ + i );
                // 插入退费数据到库里
                int rows = lisOperateRelateMapper.addLabBarcodeCreateCancel(mapParam);
                if(rows < 1) throw new Exception("添加计费数据失败！");
                // 更新已退数量
                hashMap.put("TFGL", mapParam.get("TFGL"));
                int rowsYlsl  = lisOperateRelateMapper.updateYtsl(hashMap);
                if(rowsYlsl < 1) throw new Exception("更新已退数量失败！");
                i++;
            }
        }
        if(i == 0){
            return ResultMessage.error(500,"未查询到已收费数据，无法进行退费处理");
        }
        // 更新执行标志
        Map<String,Object> map = new HashMap<>();
        map.put("YZXHS",this.getYzxh(list));
        int rowsExecutionSign  = lisOperateRelateMapper.updateExecutionSign(map);
        if(rowsExecutionSign < 1) throw new Exception("更新【zy_bqyz】执行标志失败;【"+map+"】");
        return ResultMessage.success(200,"");
    }




    /**
     * 住院his计费
     * @param paramJson
     * @return
     */
    @Override
    @RequestMapping(value = "/lisPay")
    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity lisPay(String paramJson) throws Exception {
        List<Map<String,Object>> hashMap = (List<Map<String,Object>>) JSONArray.parse(paramJson);
        long jlxh = queryHISKeyCommon.getMaxANDValide("ZY_FYMX", hashMap.size());
        return hisCharge(hashMap , jlxh);
    }

    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity hisCharge(List<Map<String,Object>> hashMap, long DQZ) throws Exception {
        List<Map> hashParas = new ArrayList<Map>();
        if(hashMap != null && hashMap.size() > 0) {
            for (int i = 0; i < hashMap.size(); i++) {
                Map<String, Object> mappara = hashMap.get(i);
                // 得到jlxh
//                long jlxh = queryHISKeyCommon.getMaxANDValide("ZY_FYMX", 1);
//                mappara.put("JLXH", jlxh);
//                mappara.put("TFGL", jlxh);
                mappara.put("JLXH", DQZ + i);
                mappara.put("TFGL", DQZ + i);
                //费用归并
                String fyxm = lisOperateRelateMapper.getFyxm(mappara);
                mappara.put("FYXM", fyxm);
                //支付比例
                double Zfbl = Double.parseDouble(lisOperateRelateMapper.getZfbl(mappara));
                mappara.put("ZFBL", Zfbl);
                double zjje = Double.parseDouble((mappara.get("ZJJE").toString()));
                double zfje = Zfbl * zjje;
                mappara.put("ZFJE", zfje);
                //屏蔽老版取zlxz
//                int zlxz = lisOperateRelateMapper.getZlxz(mappara);
                int zlxz = lisOperateRelateMapper.getNewZlxz(mappara);
                if(zlxz==0){
                    zlxz = lisOperateRelateMapper.getMinZlxz(mappara);
                }
                mappara.put("ZLXZ", zlxz);

                int rel = lisOperateRelateMapper.getZYBQYZCount(mappara);
                if(rel == 0){
                    return ResultMessage.success(500,"该病区医嘱已作废,请核实数据;【"+mappara+"】");
                }

                //查询病区医嘱的医嘱序号
//                Map temp = new HashMap();
//                temp.putAll(mappara);
                //此段代码注释，因为用的LIS，l_jytmxx_mx中jlxh
//                String yzxh = lisOperateRelateMapper.getYZXH(temp);
//                if("".equals(yzxh) || null == yzxh || "null".equals(yzxh) || yzxh.length() == 0){
//                    temp.put("FYXH", "");
//                    yzxh = lisOperateRelateMapper.getYZXH(temp);
//                    if("".equals(yzxh) || null == yzxh || "null".equals(yzxh) || yzxh.length() == 0){
//                        throw new Exception("获取YZXH失败,为空");
//                    }
//                }
//                mappara.put("YZXH", yzxh);

                hashParas.add(mappara);
                // 需更新状态
                int updateRows = lisOperateRelateMapper.updateMedicalOrderExecutionStatus(mappara);
                if (updateRows < 0) {
                    throw new Exception("更新住院病区医嘱失败");
                }
            }
            // 添加检验明细计费明细
            lisOperateRelateMapper.addZYLabRequestPayBill(hashParas);
        }
        return ResultMessage.success(200,"");
    }



    /**
     * 条码打印HIS门诊更新状态
     * @param paramJson
     * @return
     */
    @Override
    @RequestMapping(value = "/updateMzZxbz")
    @Transactional(rollbackFor=Exception.class)
    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    public ResponseEntity updateMzZxbz(String paramJson) throws Exception {
        Map<String,Object> map = (Map<String,Object>) JSONArray.parse(paramJson);
        Map labBarcode = (Map) map.get("LabBarcode");
        Map exeDocMap = (Map)labBarcode.get("ExeDoctor");
        Object labRequest  =  labBarcode.get("LabRequest");
        List<HashMap<String,Object>> requestList = (List<HashMap<String,Object>>) labRequest;
        String requestId = null;
        for (Map<String,Object> request : requestList){
            requestId = (String)request.get("RequestId");
            request.put("ITEMCODE_TEXT", this.getItemCode(request));
            String exeDateTime = (String) labBarcode.get("ExeDateTime");
            request.put("ExeDateTime_TEXT", exeDateTime.replaceAll("T"," "));
            request.put("EXEDOCTOR_TEXT", exeDocMap.get("#text"));
            lisOperateRelateMapper.updateMzZxbz(request);
        }
        return ResultMessage.success(200,"");
    }

    /**
     * 查询zy_bqyz
     * @param paramJson
     * @return
     */
    @Override
    @RequestMapping("/queryYZXHStr")
    @Transactional(rollbackFor=Exception.class)
    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    public ResponseEntity queryYZXHStr(String paramJson) {
        Map<String,Object> map = (Map<String,Object>) JSONArray.parse(paramJson);
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("YSYZBH" , map.get("YSYZBH")) ;
        paramMap.put("REQUESTID" , map.get("REQUESTID")) ;
        paramMap.put("FYXH" , map.get("YLXH")) ;
        String yzxh = lisOperateRelateMapper.getYZXH(paramMap);
        if("".equals(yzxh) || null == yzxh || "null".equals(yzxh) || yzxh.length() == 0){
            paramMap.put("FYXH", "");
            yzxh = lisOperateRelateMapper.getYZXH(paramMap);
            if("".equals(yzxh) || null == yzxh || "null".equals(yzxh) || yzxh.length() == 0){
                return ResultMessage.error(500,"");
            }
        }
        return ResultMessage.success(200,"",yzxh);
    }


    /**
     * 描述：得到所有的YZXH(jlxh)
     * @param map
     * @return
     * @return List
     */
    @SuppressWarnings("rawtypes")
    public List getYzxh(List<Map<String, Object>> map){
        List<Object> list = new ArrayList<>();
        for (Map hashMap : map) {
            list.add(hashMap.get("YZXH"));
        }
        return list;
    }
    /**
     * 描述：取 LabBarcode 节点下 ExeDoctor 的值
     * @param map
     * @return
     * @return String
     */
    @SuppressWarnings({ "rawtypes" })
    public String getExeDoctor(Map map){
        Map labBarcode = (Map) map.get("LabBarcode");
        Map ExeDoctor = (Map) labBarcode.get("ExeDoctor");
        String ExeDoctorNum =  (String) ExeDoctor.get("#text");
        return ExeDoctorNum;
    }

    public Object getItemCode(Map<String,Object> map) throws Exception{
        List<String> itemCode = new ArrayList<String>();
        if(map.get("Item") instanceof List){
            List<Map> item  = (List<Map>) map.get("Item");
            for (int i = 0; i < item.size(); i++) {
                Map map1 = (Map) item.get(i).get("ItemCode");
                itemCode.add((String)map1.get("#text"));
            }
        }else{
            Map item  = (Map)map.get("Item");
            Map map1 = (Map) item.get("ItemCode");
            itemCode.add((String)map1.get("#text"));
        }
        return itemCode;
    }
}
