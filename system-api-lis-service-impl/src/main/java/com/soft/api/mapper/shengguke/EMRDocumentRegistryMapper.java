package com.soft.api.mapper.shengguke;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface EMRDocumentRegistryMapper {

    public int testLcn(Map<String,Object> map);

    /**
     * 更新检验条码样本类型
     * @param map
     * @return
     */
    public int updateJytmxxSampleType(Map<String,Object> map);

    /**
     * 更新检验条码中样本号
     * @param map
     * @return
     */
    public int updateJytmxxSampleNo(Map<String,Object> map);

    /**
     * 获取细菌ID
     * @param map
     * @return
     */
    public Map getBioID(Map<String,Object> map);

    /**
     * 获取药敏ID
     * @param map
     * @return
     */
    public Map getAntiID(Map<String,Object> map);

    /**
     * 获取药敏TESTID
     * @param map
     * @return
     */
    public Map getTestIDByANIT(Map<String,Object> map);

    /**
     * 获取细菌TESTID
     * @param map
     * @return
     */
    public Map getTestIDByBIO(Map<String,Object> map);

    /**
     * 是否已有药敏结果
     * @param map
     * @return
     */
    public int isAntiResult(Map<String,Object> map);

    /**
     * 是否已有细菌结果
     * @param map
     * @return
     */
    public int isBioResult(Map<String,Object> map);

    /**
     * 获取药敏字典
     * @param map
     * @return
     */
    public Map<String,Object> getAntiDic(Map<String,Object> map);

    /**
     * 更新药敏结果
     * @param map
     * @return
     */
    public int updateAntiResult(Map<String,Object> map);

    /**
     * 新增药敏结果
     * @param map
     * @return
     */
    public int insertAntiResult(Map<String,Object> map);

    /**
     * 更新微生物细菌表
     * @param map
     * @return
     */
    public int updateBioResult(Map<String,Object> map);

    /**
     * 新增微生物细菌表
     * @param map
     * @return
     */
    public int insertBioResult(Map<String,Object> map);

    /**
     * 查询出lis的样本类型
     * @param map
     * @return
     */
    public Map<String,Object> getSampleType(Map<String,Object> map);

    /**
     * 写入培养结果表<br>
     * @param map
     * @return
     */
    public int insertPlantResult(Map<String,Object> map);

    /**
     * 根据第三方的培养编码对照出HIS<br>
     * l_channel 表维护<br>
     * @param map
     * @return
     */
    public Map<String,Object> getTestIDBy(Map<String,Object> map);

    /**
     * 更新微生物检验结果PlantResult<br>
     * @param map
     * @return
     */
    public int updatePlantResult(Map<String,Object> map);

    /**
     * 判断微生物结果表是否已经有数据
     * @param map
     * @return
     */
    public int isPlantResult(Map<String,Object> map);

    /**
     * 判断是否已审核
     * @param map
     * @return
     */
    public int isReview(Map<String,Object> map);

    /**
     * 删除患者信息
     * @param map
     * @return
     */
    public int deletePatientInfo(Map<String,Object> map);

    /**
     * 从检验条码信息表获取出患者相关信息
     * @param map
     * @return
     */
    public HashMap<String,Object> getJyTmXx(Map<String,Object> map);

    /**
     * 新增患者信息
     * @param map
     * @return
     */
    public int insertPatientInfo(Map<String,Object> map);

    /**
     * 删除一般检验结果
     * @param map
     * @return
     */
    public int delTestResult(Map<String,Object> map);

    /**
     * 自定义参数
     * @param map
     * @return
     */
    public HashMap<String,Object> queryEMRDocumentRegistryParaD(Map<String,Object> map);

    /**
     * 保存testResult
     * @param list
     * @return
     */
    public int addEMRDocumentRegistryD(List list);

    /**
     * 查询testID
     * @param map
     * @return
     */
    public List<String> queryTestId(Map<String,Object> map);

    /**
     * 新增结果描述
     * @param map
     * @return
     */
    public int addLtestdescribe(List<Map<String, Object>> map);
}
