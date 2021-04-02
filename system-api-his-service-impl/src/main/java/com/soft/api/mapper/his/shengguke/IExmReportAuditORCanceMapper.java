package com.soft.api.mapper.his.shengguke;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface IExmReportAuditORCanceMapper {

    /**
     * 判断是否存在报告
     * @return
     */
    public int isReportReview(Map<String,Object> map);

    /**
     * 更新审核通知
     * @param map
     * @return
     */
    public int updateJcztStatusAudit(Map<String,Object> map);

    /**
     * 更新取消审核通知
     * @param map
     * @return
     */
    public int updateJcztStatusCancel(Map<String,Object> map);

    /**
     * 根据ID删除报告
     * @param map
     * @return
     */
    public int deleteJCBGByID(Map<String,Object> map);

}
