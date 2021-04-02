package com.soft.api.service.his.shengguke;

import com.soft.api.mapper.his.shengguke.IExmRequestExecutedCancelMapper;
import ctd.net.rpc.util.DateUtil;
import ctd.net.rpc.util.MapUtil;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 取消检查登记执行通知
 * @ClassName ExmRequestExecutedCancelImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/8
 * @Version V1.0
 **/
@Service
public class ExmRequestExecutedCancelImpl {

    @Autowired
    private QueryHISKeyCommon queryHISKeyCommon;

    @Autowired
    private IExmRequestExecutedCancelMapper exmRequestExecutedCancelMapper;

    /**
     * 计费取消执行通知 <br>
     * HIS触发器 TRI_ZY_FYMX_INSERT 报错：ORA-04091:  触发器/函数不能读它<br>
     * 解决办法：在触发器DECLARE中加以下：<br>
     * DECLARE
     * --声明使用自治事务 <br>
     *   PRAGMA AUTONOMOUS_TRANSACTION; <br>
     * @param map
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity exmRequestExecutedCancel(Map<String,Object> map) throws Exception {
        Map mzZyMap = exmRequestExecutedCancelMapper.getMzOrZy(map);
        if(mzZyMap == null || mzZyMap.size() == 0){
            throw new Exception("未能查询出门诊/住院");
        }
        int mzZy = Integer.parseInt(mzZyMap.get("MZZY").toString());
        // 获取所有的item 节点下的ItemCode值 再写入map
        map.put("ITEMCODES", MapUtil.getItemCodes(map, "ExmRequest"));
        if(mzZy == 1){
            // 门诊
            String operateDateTime = DateUtil.strToDateFormat(this.getExmRequestNode(map) ,"T");
            String operator = this.getOperator(map);
            List<Map<String, Object>> list = exmRequestExecutedCancelMapper.getYjxhs(map);
            if(list != null && list.size() > 0){
                //判断检查是否已执行,不能重复执行
                int rel = exmRequestExecutedCancelMapper.isExc(list);
                if(rel > 0){
                    throw new Exception("门诊检查已经取消执行不能重复执行,【YJXH】值为【"+list+"】;参数【"+map+"】");
                }
                for (Map<String, Object> hashMap : list) {
                    int executionStatus = exmRequestExecutedCancelMapper.updateExecutionStatus(hashMap);
                    if(executionStatus < 0) throw new Exception("更新检查项目执行状态失败！");
                    //判断检查项目是否执行 如果返回 == 0
                    int yes = exmRequestExecutedCancelMapper.projectIsExecution(hashMap);
                    if(yes == 0){
                        int jcztRows1 = exmRequestExecutedCancelMapper.updateJczt(hashMap);
                        if(jcztRows1 < 1) throw new Exception("更新检查项目执行状态失败！");
                    }
                    List<Map<String, Object>> listPara = exmRequestExecutedCancelMapper.queryExmRequestPayBillDParam(hashMap);
                    List<Map<String, Object>> newListPara = new ArrayList<Map<String, Object>>();
                    if(listPara != null && listPara.size() > 0){
                        for (Map<String, Object> hashMap2 : listPara) {
                            long jlxh = queryHISKeyCommon.getMaxANDValide("YJ_BG01_JJHS",1);
                            hashMap2.put("JLXH", jlxh);
                            // 添加JCRQ到map  (取传入参数map ExeDateTime节点的值)
                            hashMap2.put("JCRQ", operateDateTime);
                            // 添加JCYS到map  (取传入参数map Doctor节点的值)
                            hashMap2.put("JCYS", operator);
                            newListPara.add(hashMap2);
                        }
                        // 插入检查经济核算明细
                        int rows1 = exmRequestExecutedCancelMapper.addExmRequestPayBillD(newListPara);
                        if(rows1 < 1) throw new Exception("插入检查经济核算明细失败！");
                        // 先根据yjxh删除数据
                        exmRequestExecutedCancelMapper.deleteExmYjxh(hashMap);
                    }else{
                        throw new Exception("根据JCSQ中YJXH未在MSYJ01中查询到参数;无法进行取消执行");
                    }
                    //更行执行项目的医生、日期
                    map.put("YJXH", hashMap.get("YJXH"));
                    int updateRows = exmRequestExecutedCancelMapper.updateExecutionDoctor(map);
                    if(updateRows < 1) throw new Exception("更新执行项目的医生、日期失败！");
                }
            }else{
                throw new Exception("没有查询到所有医技序号!【"+map+"】");
            }
        }else if(mzZy == 2){
            // 住院
            int jifei = 0;
            String operateDateTime = DateUtil.strToDateFormat(this.getExmRequestNode(map), "T");
            String operator = this.getOperator(map);
            List<Map<String, Object>> list = exmRequestExecutedCancelMapper.getYjxhsZY(map);
            if(list != null && list.size() > 0){
                //判断是否已执行
                int rel = exmRequestExecutedCancelMapper.isZYExc(list);
                if(rel > 0){
                    throw new Exception("住院检查已经取消执行不能重复取消执行,【YJXH】值为【"+list+"】;参数【"+map+"】");
                }
                for (Map<String, Object> hashMap : list) {
                    int executionStatus = exmRequestExecutedCancelMapper.updateExecutionStatus(hashMap);
                    if(executionStatus < 0) throw new Exception("更新检查项目执行状态失败！");
                    //判断检查项目是否执行 如果返回 == 0
                    int yes = exmRequestExecutedCancelMapper.projectIsExecution(hashMap);
                    if(yes == 0){
                        int jcztRows1 = exmRequestExecutedCancelMapper.updateJczt(hashMap);
                        if(jcztRows1 < 1) throw new Exception("更新检查项目执行状态失败！");
                    }
                    //删除bg01
                    exmRequestExecutedCancelMapper.deleteExmYjxhZY(hashMap);
                    //更行执行项目的医生
                    map.put("YJXH", hashMap.get("YJXH"));
                    map.put("YZXH", hashMap.get("YZXH"));
                    //住院取消医技执行需要作废YJ
                    int rows2 = exmRequestExecutedCancelMapper.updateExecutionDoctorZY(map);
                    if(rows2 < 1) throw new Exception("住院医技执行更新失败！");
                    int rows3 = exmRequestExecutedCancelMapper.updateMedicalOrderExecutionZY(map);
                    if(rows3 < 1) throw new Exception("检查医嘱执行更新失败！");
                }
                List<Map<String, Object>> listMap = exmRequestExecutedCancelMapper.queryMedicalOrderExecutionParaZY(list);
                List<Map<String, Object>> newListMap = new ArrayList<Map<String, Object>>();
                if(listMap != null && listMap.size() > 0){
                    for (Map<String, Object> hashMap2 : listMap) {
                        long jlxh = queryHISKeyCommon.getMaxANDValide("YJ_BG01_JJHS",1);
                        hashMap2.put("JLXH", jlxh);
                        hashMap2.put("JCYS", operator);
                        hashMap2.put("JCRQ", operateDateTime);
                        newListMap.add(hashMap2);
                    }
                    int addRows = exmRequestExecutedCancelMapper.addMedicalOrderExecutionZY(newListMap);
                    if(addRows < 1) throw new Exception("添加取消住院医技执行失败！");
                }else{
                    throw new Exception("没有查询到医技执行！");
                }
                /**
                 * ------------------------------退费开始-------------------
                 */
                List<Map<String, Object>> lists = exmRequestExecutedCancelMapper.queryExmRequestPayBillParaZY(list);
                List<Map<String, Object>> newLists = new ArrayList<Map<String, Object>>();
                List<Object> listTfgl = new ArrayList<Object>();
                if(lists != null && lists.size() > 0){
                    for (Map<String, Object> hashMap2 : lists) {
                        long getJlxh = queryHISKeyCommon.getMaxANDValide("ZY_FYMX",1);
                        hashMap2.put("JLXH", getJlxh);
                        hashMap2.put("SRGH", operator);
                        Object tfgl = exmRequestExecutedCancelMapper.getTfglZY(hashMap2);
                        hashMap2.put("TFGL", tfgl);
                        newLists.add(hashMap2);
                        listTfgl.add(tfgl);
                    }
                    jifei = exmRequestExecutedCancelMapper.addExmPayBillZY(newLists);
                    if(jifei < 1) throw new Exception("住院退费失败！");
                    int ytslRows = exmRequestExecutedCancelMapper.updateZyZymxYtsl(listTfgl);
                    if(ytslRows < 1) throw new Exception("更新已退数量失败！");
                }
            }else{
                throw new Exception("没有查询到所有住院的医技序号!");
            }
        }else{
            throw new Exception("该申请单不属于门诊或者住院！");
        }
        return ResultMessage.error(200,"取消检查登记执行通知成功");
    }


    /**
     * 描述：取Item节点下  OperateDateTime 的值
     * @param map
     * @return
     * @return String
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String getExmRequestNode(Map map){
        String operateDateTime = null;
        Map exmRequest = (Map) map.get("ExmRequest");
        if(exmRequest.get("Item") instanceof List){
            List<Map> listMap = (ArrayList<Map>) exmRequest.get("Item");
            operateDateTime = (String) listMap.get(0).get("OperateDateTime");
        }else{
            Map mapItem = (Map) exmRequest.get("Item");
            operateDateTime = (String) mapItem.get("OperateDateTime");
        }
        return operateDateTime;
    }
    /**
     * 描述：取Item节点下  Operator 的值
     * @param map
     * @return
     * @return String
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String getOperator(Map map){
        String operator = null;
        Map exmRequest = (Map) map.get("ExmRequest");
        if(exmRequest.get("Item") instanceof List){
            List<Map> listMap = (ArrayList<Map>) exmRequest.get("Item");
            if(listMap.get(0).get("Operator") instanceof Map){
                Map exeDoctorMap = (Map) listMap.get(0).get("Operator");
                operator = (String) exeDoctorMap.get("#text");
            }else{
                operator = listMap.get(0).get("Operator").toString();
            }
        }else{
            Map mapItem = (Map) exmRequest.get("Item");
            if(mapItem.get("Operator") instanceof Map){
                Map exeDoctorMap = (Map) mapItem.get("Operator");
                operator = (String) exeDoctorMap.get("#text");
            }else{
                operator = mapItem.get("Operator").toString();
            }
        }
        return operator;
    }
}
