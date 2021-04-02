package com.soft.api.mapper.his.zigong;

import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 手术相关
 */
@Repository
public interface OperationMapper {

    public int updateArrangeWC(Map<String,Object> map);

    public int updateArrangeSSMZ(Map<String,Object> map);

    public int insertOperationReCordOld(Map<String,Object> map);

    public Map<String,Object> queryOpertionWCjlByRequestId(Map<String,Object> map);

    public Map<String,Object> queryOpertionRecordByRequestId(Map<String,Object> map);

    public int updateArrangeBZ(Map<String,Object> map);

    public int updateArrangeStatus(Map<String,Object> map);

    public Map<String,Object> querySMSSAPInfo(Map<String,Object> map);

    public Map<String,Object> queryZYH(Map<String,Object> map);

    public Map<String,Object> queryStatus(Map<String,Object> map);

    public int updateArrange(Map<String,Object> map);

    public int addArrange(Map<String,Object> map);
}
