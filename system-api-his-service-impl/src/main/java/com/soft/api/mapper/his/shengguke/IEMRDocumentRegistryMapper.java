package com.soft.api.mapper.his.shengguke;

import org.springframework.stereotype.Repository;
import java.util.Map;

@Repository
public interface IEMRDocumentRegistryMapper {
    public int test(Map<String,Object> map);

    /**
     * 获取检查项目的医嘱序号
     * @param map
     * @return
     */
    public Map getJcxmYzxh(Map<String,Object> map);

    /**
     * 更新检查申请JCZT为4,已出报告
     * @param map
     * @return
     */
    public int updateJCSQ(Map<String,Object> map);

    /**
     * 判断当前报告是否已审核
     * @param map
     * @return
     */
    public int isReview(Map<String,Object> map);

    /**
     * 查询emr检查申请
     * @param map
     * @return
     */
    public Map<String,Object> queryDocumentRegistryPUEPara(Map<String,Object> map);

    /**
     * 删除EMR_JCBG检查报告
     * @param map
     * @return
     */
    public int deleteBySqdh(Map<String,Object> map);

    /**
     * 新增报告
     * @param map
     * @return
     */
    public int addDocumentRegistryPUE(Map<String,Object> map);


}
