package com.soft.api.mapper.his.shengguke;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IExmRequestExecutedCancelMapper {

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
     * 更新zy_fymx已退数量
     * @param list
     * @return
     */
    public int updateZyZymxYtsl(List list);

    /**
     * 插入ZY_FYMX
     * @param list
     * @return
     */
    public int addExmPayBillZY(List list);

    /**
     * 获取TFGL
     * @param map
     * @return
     */
    public String getTfglZY(Map<String, Object> map);

    /**
     * 查询支付相关参数
     * @param list
     * @return
     */
    public List<Map<String, Object>> queryExmRequestPayBillParaZY(List<Map<String, Object>> list);

    /**
     * 插入YJ_BG01_JJHS
     * @param list
     * @return
     */
    public int addMedicalOrderExecutionZY(List list);

    /**
     * 查询支付参数
     * @param list
     * @return
     */
    public List<Map<String, Object>> queryMedicalOrderExecutionParaZY(List list);

    /**
     * 更新ZY_BQYZ
     * @param map
     * @return
     */
    public int updateMedicalOrderExecutionZY(Map<String,Object> map);

    /**
     * 更新YJ_ZY01
     * @param map
     * @return
     */
    public int updateExecutionDoctorZY(Map<String,Object> map);

    /**
     * 删除YJ_BG01
     * @param map
     * @return
     */
    public int deleteExmYjxhZY(Map<String,Object> map);

    /**
     *  获取 YJXH, SQDH,  YZXH,  ZLXMID
     * @param map
     * @return
     */
    public List<Map<String, Object>> getYjxhsZY(Map<String,Object> map);

    /**
     * 更新执行医生
     * @param map
     * @return
     */
    public int updateExecutionDoctor(Map<String,Object> map);

    /**
     * 删除YJ_BG01
     * @param map
     * @return
     */
    public int deleteExmYjxh(Map<String,Object> map);

    /**
     * 插入YJ_BG01_JJHS
     * @param list
     * @return
     */
    public int addExmRequestPayBillD(List list);

    /**
     * 查询支付参数
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryExmRequestPayBillDParam(Map<String,Object> map);

    /**
     *
     * @param map
     * @return
     */
    public int updateJczt(Map<String,Object> map);

    /**
     * 判断检查项目是否执行 如果返回> 1 则执行updateJczt
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
     * 获取参数SQDH,  ZLXMID, YJXH
     * @param map
     * @return
     */
    public List<Map<String,Object>> getYjxhs(Map<String,Object> map);

    /**
     * 判断是门诊/住院
     * @param map
     * @return
     */
    public Map getMzOrZy(Map<String,Object> map);

}
