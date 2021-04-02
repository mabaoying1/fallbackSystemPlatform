package com.soft.api.mapper.his.shengguke;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IExmRequestExecutedMapper {

    /**
     * 判断住院是否已执行
     * @param list
     * @return
     */
    public int isZYExc(List list);

    /**
     * 判断门诊是否已执行
     * @param list
     * @return
     */
    public int isExc(List list);

    /**
     * 住院检查医嘱执行
     * @param map
     * @return
     */
    public int updateMedicalOrderExecutionZY(Map<String,Object> map);

    /**
     * 住院医技执行
     * @param map
     * @return
     */
    public int updateExecutionDoctorZY(Map<String,Object> map);

    /**
     * 插入ZY_FYMX
     * @param list
     * @return
     */
    public int addExmPayBillZY(List list);

    /**
     *
     * @param list
     * @return
     */
    public List<Map<String, Object>> queryExmRequestPayBillParaZY(List list);

    /**
     * 住院删除YJ_BG01
     * @param list
     * @return
     */
    public int deleteExmYjxhZY(List list);

    /**
     * 住院新增YJ_BG01_JJHS
     * @param list
     * @return
     */
    public int addMedicalOrderExecutionZY(List list);

    /**
     * 查询住院相关参数
     * @param list
     * @return
     */
    public List<Map<String,Object>> queryMedicalOrderExecutionParaZY(List<Map<String, Object>> list);

    /**
     * 住院查询 YJXH, SQDH, YZXH
     * @param map
     * @return
     */
    public List<Map<String,Object>> getYjxhsZY(Map<String,Object> map);

    /**
     * 更新 MS_YJ01
     * @param map
     * @return
     */
    public int updateExecutionDoctor(Map<String,Object> map);

    /**
     * 新增YJ_BG01
     * @param map
     * @return
     */
    public int addExmRequestPayBill(Map<String,Object> map);

    /**
     * 删除YJ_BG01
     * @param map
     * @return
     */
    public int deleteExmYjxh(Map<String,Object> map);

    /**
     * 插入经济核算表
     * @param list
     * @return
     */
    public int addExmRequestPayBillD(List list);

    /**
     * 查询申请单支付相关参数
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryExmRequestPayBillDParam(Map<String,Object> map);

    /**
     * 更新EMR_JCSQ状态
     * @param map
     * @return
     */
    public int updateJczt(Map<String,Object> map);

    /**
     * 判断检查项目是否执行
     * @param map
     * @return
     */
    public int projectIsExecution(Map<String,Object> map);

    /**
     * 更新检查项目执行状态
     * @param map
     * @return
     */
    public int updateExecutionStatus(Map<String,Object> map);

    /**
     * 获取SQDH、YJXH、ZLXMID
     * @param map
     * @return
     */
    public List<Map<String,Object>> getYjxhs(Map<String,Object> map);

    /**
     * 判断门诊OR住院
     * @param map
     * @return
     */
    public Map getMzOrZy(Map<String,Object> map);

    /**
     * 查询zlxz
     * @param hashMap2
     * @return
     */
    public Integer getNewZlxz(Map<String, Object> hashMap2);
    /**
     * 如果
     * @param hashMap2
     * @return
     */
    public Integer getMinZlxz(Map<String, Object> hashMap2);
}
