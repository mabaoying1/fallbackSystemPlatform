package com.soft.api.service.his.shengguke;

import com.soft.api.mapper.his.shengguke.IExmRequestExecutedMapper;
import ctd.net.rpc.util.DateUtil;
import ctd.net.rpc.util.MapUtil;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 检查登记执行通知
 * @ClassName CheckRelateImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/7
 * @Version V1.0
 **/
@Service
public class ExmRequestExecutedImpl {

    private final Logger logger = LoggerFactory.getLogger(ExmRequestExecutedImpl.class);

    @Autowired
    private IExmRequestExecutedMapper exmRequestExecutedMapper;

    @Autowired
    private QueryHISKeyCommon queryHISKeyCommon;
    /**
     * 检查登记执行通知
     * @param map
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity exmRequestExecuted(Map<String,Object> map) throws Exception {
        Map mzZyMap = exmRequestExecutedMapper.getMzOrZy(map);
        if(mzZyMap == null || mzZyMap.size() == 0){
            throw new Exception("未能查询出门诊/住院");
        }
        int mzZy = Integer.parseInt(mzZyMap.get("MZZY").toString());
        // 获取所有的item 节点下的ItemCode值 再写入map
        map.put("ITEMCODES", MapUtil.getItemCodes(map, "ExmRequest"));
        if (mzZy == 1) {
            // 门诊
            String exeDateTime = DateUtil.strToDateFormat(this.getExmRequestNode(map), "T");
            String exeDoctor = this.getExeDoctor(map);
            List<Map<String, Object>> list = exmRequestExecutedMapper.getYjxhs(map);
            if (list != null && list.size() > 0) {
                //判断检查是否已执行,不能重复执行
                int rel = exmRequestExecutedMapper.isExc(list);
                if(rel > 0){
                    return ResultMessage.success(200, "门诊检查已经执行不能重复执行,【YJXH】值为【"+list+"】;参数【"+map+"】");
//                    throw new Exception("门诊检查已经执行不能重复执行,【YJXH】值为【"+list+"】;参数【"+map+"】");
                }
                for (Map<String, Object> hashMap : list) {
                    //更新检查项目状态
                    int executionStatus = exmRequestExecutedMapper.updateExecutionStatus(hashMap);
                    if (executionStatus < 1)
                        throw new Exception("更新检查项目执行状态失败！");
                    // 判断检查项目是否执行 如果返回> 1
                    int yes = exmRequestExecutedMapper.projectIsExecution(hashMap);
                    if (yes == 0) { //更新JCSQ状态
                        int jcztRows1 = exmRequestExecutedMapper.updateJczt(hashMap);
                        if (jcztRows1 < 1)
                            throw new Exception("更新检查项目执行状态失败！");
                    }
                    List<Map<String, Object>> listPara = exmRequestExecutedMapper
                            .queryExmRequestPayBillDParam(hashMap);
                    List<Map<String, Object>> newListPara = new ArrayList<Map<String, Object>>();
                    Map<String, Object> yjzxMap = null;
                    double hjje = 0;
                    if (listPara != null && listPara.size() > 0) {
                        for (Map<String, Object> hashMap2 : listPara) {
                            long jlxh = queryHISKeyCommon.getMaxANDValide("YJ_BG01_JJHS",1);
                            hashMap2.put("JLXH", jlxh);
                            // 添加JCRQ到map (取传入参数map ExeDateTime节点的值)
                            // exeDateTime = this.getExmRequestNode(map);
                            hashMap2.put("JCRQ", exeDateTime);
                            // 添加JCYS到map (取传入参数map Doctor节点的值)
                            hashMap2.put("JCYS", exeDoctor);
                            newListPara.add(hashMap2);
                            // 计算出总的hjje
                            BigDecimal hjje1 = (BigDecimal) hashMap2.get("HJJE");
                            hjje += hjje1.doubleValue();
                            BigDecimal yjzx = (BigDecimal) hashMap2.get("YJZX");
                            if (yjzx.intValue() == 1) {
                                yjzxMap = hashMap2;
                            }
                        }
                        // 插入检查经济核算明细
                        int rows1 = exmRequestExecutedMapper.addExmRequestPayBillD(newListPara);
                        if (rows1 < 1)
                            throw new Exception("插入检查经济核算明细失败！");
                        // 先根据yjxh删除数据
                        exmRequestExecutedMapper.deleteExmYjxh(hashMap);
                        if (yjzxMap == null) {
                            throw new Exception("没有找到到yjzx等于1的数据！");
                        }
                        // 把计算出的总金额 放入map
                        yjzxMap.put("HJJE", hjje);
                        // 添加检查经济核算
                        int rows3 = exmRequestExecutedMapper.addExmRequestPayBill(yjzxMap);
                        if (rows3 < 1)
                            throw new Exception("添加检查经济核算失败！");
                    } else {
                        throw new Exception("没有查询到插入的参数！");
                    }
                    // 更行执行项目的医生、日期
                    map.put("ZXRQ", exeDateTime);
                    map.put("HJGH", exeDoctor);
                    // map.put("ZXYS", this.getExeDoctor(map));
                    map.put("YJXH", hashMap.get("YJXH"));
                    int updateRows = exmRequestExecutedMapper.updateExecutionDoctor(map);
                    if (updateRows < 1)
                        throw new Exception("更新执行项目的医生、日期失败！");
                }
            } else {
                throw new Exception("没有查询到所有医技序号!");
            }
        } else if (mzZy == 2) {
            // 住院
            int jifei = 0;
            String exeDateTime = DateUtil.strToDateFormat(this.getExmRequestNode(map), "T");
            String exeDoctor = this.getExeDoctor(map);
            List<Map<String, Object>> list = exmRequestExecutedMapper.getYjxhsZY(map);
            if (list != null && list.size() > 0) {
                //判断是否已执行
                int rel = exmRequestExecutedMapper.isZYExc(list);
                if(rel > 0){
                    return ResultMessage.success(200, "住院检查已经执行不能重复执行,【YJXH】值为【"+list+"】;参数【"+map+"】");
//                    throw new Exception("住院检查已经执行不能重复执行,【YJXH】值为【"+list+"】;参数【"+map+"】");
                }
                List<Map<String, Object>> listMap = exmRequestExecutedMapper.queryMedicalOrderExecutionParaZY(list);
                List<Map<String, Object>> newListMap = new ArrayList<Map<String, Object>>();
                if (listMap != null && listMap.size() > 0) {
                    double hjje = 0;
                    Map<String, Object> yjzxMap = null;
                    for (Map<String, Object> hashMap2 : listMap) {
                        long jlxh = queryHISKeyCommon.getMaxANDValide("YJ_BG01_JJHS",1);
                        hashMap2.put("JLXH", jlxh);
                        hashMap2.put("JCYS", exeDoctor);
                        hashMap2.put("JCRQ", exeDateTime);
                        // 计算出总的hjje
                        BigDecimal hjje1 = (BigDecimal) hashMap2.get("HJJE");
                        hjje += hjje1.doubleValue();
                        BigDecimal yjzx = (BigDecimal) hashMap2.get("YJZX");
                        if (yjzx.intValue() == 1) {
                            yjzxMap = hashMap2;
                        }
                        newListMap.add(hashMap2);
                    }
                    int addRows = exmRequestExecutedMapper.addMedicalOrderExecutionZY(newListMap);
                    if (addRows < 1)
                        throw new Exception("添加住院医技执行失败！");
                    exmRequestExecutedMapper.deleteExmYjxhZY(list);
                    // 把计算出的总金额 放入map
                    if (yjzxMap == null) {
                        throw new Exception("没有找到到yjzx等于1的数据！");
                    }
                    yjzxMap.put("HJJE", hjje);
                    int rows = exmRequestExecutedMapper.addExmRequestPayBill(yjzxMap);
                    if (rows < 1)
                        throw new Exception("添加住院检查经济核算失败！");
                    /**
                     * ------------------------------计费开始-------------------
                     */
                    List<Map<String, Object>> lists = exmRequestExecutedMapper
                            .queryExmRequestPayBillParaZY(list);
                    List<Map<String, Object>> newLists = new ArrayList<Map<String, Object>>();
                    if (lists != null && lists.size() > 0) {
                        for (Map<String, Object> hashMap2 : lists) {
                            long getJlxh = queryHISKeyCommon.getMaxANDValide("ZY_FYMX",1);
                            hashMap2.put("JLXH", getJlxh);
                            hashMap2.put("SRGH", exeDoctor);
                            // hashMap2.put("TFGL", getJlxh);
                            int zlxz = exmRequestExecutedMapper.getNewZlxz(hashMap2);
                            if(zlxz==0){
                                zlxz = exmRequestExecutedMapper.getMinZlxz(hashMap2);
                            }
                            hashMap2.put("ZlXZ", zlxz);
                            newLists.add(hashMap2);
                        }
                        jifei = exmRequestExecutedMapper.addExmPayBillZY(newLists);
                        if (jifei < 1)
                            throw new Exception("住院计费失败！");
                    }else{
                        throw new Exception("未查询出住院计费参数");
                    }
                    for (Map<String, Object> hashMap : list) {
                        int executionStatus = exmRequestExecutedMapper.updateExecutionStatus(hashMap);
                        if(executionStatus < 1) throw new Exception("住院执行状态失败！");
                        // 判断检查项目是否执行 如果返回> 1
                        int yes = exmRequestExecutedMapper.projectIsExecution(hashMap);
                        if (yes == 0) {
                            int jcztRows1 = exmRequestExecutedMapper.updateJczt(hashMap);
                            if (jcztRows1 < 1)
                                throw new Exception("更新检查项目执行状态失败！");
                        }
                        // 更行执行项目的医生
                        map.put("ZXRQ", exeDateTime);
                        map.put("ZXYS", exeDoctor);
                        map.put("YJXH", hashMap.get("YJXH"));
                        map.put("YZXH", hashMap.get("YZXH"));
                        int rows2 = exmRequestExecutedMapper.updateExecutionDoctorZY(map);
                        if (rows2 < 1)
                            throw new Exception("住院医技执行更新失败！");
                        int rows3 = exmRequestExecutedMapper.updateMedicalOrderExecutionZY(map);
                        if (rows3 < 1)
                            throw new Exception("检查医嘱执行更新失败！");
                    }
                } else {
                    throw new Exception("没有查询到所有住院的医技序号!");
                }
            }else{
                throw new Exception("未查询出住院相关医技信息【yj_zy01,yj_zy02】");
            }
        } else {
            throw new Exception("该申请单不属于门诊或者住院！");
        }
        return ResultMessage.success(200,"检查登记执行通知成功");
    }

    /**
     * 描述：取Item节点下 ExeDoctor 的值
     * @param map
     * @return
     * @return String
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String getExeDoctor(Map map) {
        String exeDoctor = null;
        Map exmRequest = (Map) map.get("ExmRequest");
        if (exmRequest.get("Item") instanceof List) {
            List<Map> listMap = (ArrayList<Map>) exmRequest.get("Item");
            if(listMap.get(0).get("ExeDoctor") instanceof  Map){
                Map exeDoctorMap = (Map) listMap.get(0).get("ExeDoctor");
                exeDoctor = (String) exeDoctorMap.get("#text");
            }else{
                exeDoctor = listMap.get(0).get("ExeDoctor").toString();
            }
        } else {
            Map mapItem = (Map) exmRequest.get("Item");
            if(mapItem.get("ExeDoctor") instanceof  Map){
                Map exeDoctorMap = (Map) mapItem.get("ExeDoctor");
                exeDoctor = (String) exeDoctorMap.get("#text");
            }else{
                exeDoctor = mapItem.get("ExeDoctor").toString();
            }
        }
        return exeDoctor;
    }

    /**
     * 描述：取Item节点下 ExeDateTime 的值
     * @param map
     * @return
     * @return String
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String getExmRequestNode(Map map) {
        String exeDateTime = null;
        Map exmRequest = (Map) map.get("ExmRequest");
        if (exmRequest.get("Item") instanceof List) {
            List<Map> listMap = (ArrayList<Map>) exmRequest.get("Item");
            exeDateTime = (String) listMap.get(0).get("ExeDateTime");
        } else {
            Map mapItem = (Map) exmRequest.get("Item");
            exeDateTime = (String) mapItem.get("ExeDateTime");
        }
        return exeDateTime;
    }
}
