package com.soft.api.mapper.his.shengguke;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface IPatientRegistryMapper {

    /**
     * 获取系统默认病人性质
     * @param map
     * @return
     */
    public Map getDefaultBRXZ(Map<String,Object> map);

    /**
     * 新增KHBRDA
     * @param map
     * @return
     */
    public int insertKHBrda(Map<String,Object> map);

    /**
     * 更新YGPJ的使用号码
     * @param map
     * @return
     */
    public int updateYgpjBySyhm(Map<String,Object> map);

    /**
     * 门诊号码系列  新增员工票据
     * @param map
     * @return
     */
    public int insertMzhmYgpj(Map<String,Object> map);

    /**
     * 门诊号码系列  更新系统参数
     * @param map
     * @return
     */
    public int updateMzhmXtcs(Map<String,Object> map);

    /**
     * 更新员工票据
     * @param map
     * @return
     */
    public int updateMzhmYgpj(Map<String,Object> map);

    /**
     * 获取系统参数中虚拟工号
     * @param map
     * @return
     */
    public Map getMzhmXngh(Map<String,Object> map);

    /**
     * 更新员工票据的状态
     * @param map
     * @return
     */
    public int updateYgpjBySypb(Map<String,Object> map);

    /**
     * 获取总值
     * @param map
     * @return
     */
    public Map getZzhm(Map<String,Object> map);

    /**
     * 判断是否存在档案
     * @param map
     * @return
     */
    public int getExistEmployeeByIDCard(Map<String,Object> map);

    /**
     * 获取可以使用的SYHM,用于门诊号码
     * @param map
     * @return
     */
    public Map getMzhm(Map<String,Object> map);

    /**
     * 获取领用日期
     * @param map
     * @return
     */
    public Map getLYRQ(Map<String,Object> map);

    /**
     * 新增病案档案
     * @param map
     * @return
     */
    public int insertBrda(Map<String,Object> map);
}
