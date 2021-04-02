package com.soft.api.mapper.his.shengguke;

import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface ILisOperateRelateMapper {
    public int test(Map map);

    public int cancelMSYJExecutionSign(Map<String, Object> map);

    public int getZYBQYZCount(Map<String, Object> map);

    public int addZYLabRequestPayBill(List<Map> list);

    public int updateMedicalOrderExecutionStatus(Map<String, Object> map);

    public String getYZXH(Map<String, Object> map);

    public Integer getZlxz(Map<String, Object> map);

    public String getZfbl(Map<String, Object> map);

    public String getFyxm(Map<String, Object> map);

    public int updateMzZxbz(Map<String, Object> map);

    public int updateExecutionSign(Map<String, Object> map);

    public int updateYtsl(Map<String, Object> map);

    public int addLabBarcodeCreateCancel(Map<String, Object> map);

    public List<Map<String, Object>> queryAddPara(Map<String, Object> map);

    public int queryCYPB(Map<String, Object> map);

    public int cancelExecutionSign(List<Map<String,Object>> list);

    public Integer getNewZlxz(Map<String, Object> mappara);

    public Integer getMinZlxz(Map<String, Object> mappara);
}
