package com.soft.api.mapper.shengguke;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IInspectionRelationMapper {
    public int test(Map map);

    /**
     * 通过申请单号和YLXH和PREHYID 更新l_lis_sqdmx
     * @param map
     * @return
     */
    public int cancelOVBarCodeBillingMarkNew(Map map);

    public int getSQDMXOVCount(Map map);

    /**
     * 查询l_jytmxx中所有条码数量，根据申请单<br>
     * @param map
     * @return
     */
    public int getBarCodeByReqStatus(Map map);

    /**
     * 查询l_jytmxx中SAMPLESTATUS = 3，根据申请单<br>
     * @param map
     * @return
     */
    public int getBarCodeByStatus(Map map);

    /**
     * 根据条码号查询出所有申请单（存在一个条码号两个申请单）<br>
     * @param map
     * @return
     */
    public List<Map> getReqByBarCode(Map map);


    /**
     * 查询申请单执行判别的条数
     * @param map
     * @return
     */
    public int getSQDMXZXPBCount(Map map);

    /**
     * 原SQL【queryLabBarcodeCreateDParam】更新（因为多个申请单打印一个条码）
     * @param map
     * @return
     */
    public List<Map> queryLabBarcodeCreateDParamNew(Map<String,Object> map);

    /**
     * 更新检验条码信息中样本状态
     * @param map
     * @return
     */
    public int updateJYTMXXSampleStatus(Map<String,Object> map);

    /**
     * 查询申请单下是否还存在条码信息
     * @param map
     * @return
     */
    public int getJytmxxCount(Map<String,Object> map);

    /**
     * 获取检验条码信息化验ID
     * @param map
     * @return
     */
    public List<Map> getLjytmxxPrehyid(Map<String,Object> map);

    /**
     * 根据条码号来查询数据
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryLabRequestPayBillZYByBarCode(Map<String,Object> map);

    /**
     * 门诊取消条码打印更新l_lis_sqdmx
     * @param map
     * @return
     */
    public int cancelOVBarCodeBillingMark(Map<String,Object> map);

    /**
     * 删除检验条码信息
     * @param map
     * @return
     */
    public int deleteJYTMXX(Map<String,Object> map);

    /**
     * 删除检验条码明细
     * @param map
     * @return
     */
    public int deleteJYTMXXMX(Map<String,Object> map);

    /**
     * 取消检验条码信息明细的状态
     * @param map
     * @return
     */
    public int updateBarCodeBillingMarkCancel(Map<String,Object> map);

    /**
     * 根据条码号查询是否存在
     * @param map
     * @return
     */
    public Map isJytmxx(Map<String,Object> map);

    /**
     * 查询检验申请单根据条码号
     * @param map
     * @return
     */
    public List<Map> getSQDIDByBarCode(Map<String,Object> map);

    /**
     * 查询申请单是否已打条码
     * @param map
     * @return
     */
    public int getSQDExcStatus(Map<String,Object> map);

    /**
     * 更新 l_lis_sqd状态
     * @param map
     * @return
     */
    public int updateSqdstatus(Map<String,Object> map);

    /**
     * 更新l_lis_sqdmx
     * @param map
     * @return
     */
    public int updateJLXH1(Map<String,Object> map);

    /**
     * 新增l_jytmxx_mx
     * @param map
     * @return
     */
    public int addLabBarcodeCreateD(Map<String,Object> map);

    /**
     * 查询申请单明细参数
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryLabBarcodeCreateDParam(Map<String,Object> map);

    /**
     * 获取费用
     * @param map
     * @return
     */
    public Double getFee(Map<String,Object> map);

    /**
     * 查询LIS申请单相关参数
     * @param map
     * @return
     */
    public Map queryLabBarcodeCreateParam(Map<String,Object> map);

    /**
     * 更新检验条码明细状态
     * @param map
     * @return
     */
    public int updateBarCodeBillingMark(Map<String,Object> map);

    /**
     * 更新申请单明细状态
     * @param map
     * @return
     */
    public int updateApplicationBillingMark(Map<String,Object> map);

    /**
     * 查询出住院检验计费添加的参数
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryLabRequestPayBillZY(Map<String,Object> map);

    /**
     * 查询返回门诊住院号码
     * @param map
     * @return
     */
    public Integer queryMzZyCode(Map<String,Object> map);

    /**
     * 新增l_jytmxx
     * @param map
     * @return
     */
    public int addLabBarcodeCreate(Map<String,Object> map);

    /**
     * 更新l_lis_sqd
     * @param map
     * @return
     */
    public int updateSqdstatusCance(Map<String,Object> map);

    /**
     * 更新l_lis_sqdmx
     * @param map
     * @return
     */
    public int cancelBarCodeBillingMark(Map<String,Object> map);

    /**
     * 查询退款的相关数据
     * @param map
     * @return
     */
    public List<Map<String,Object>> queryIsRefundData(Map<String,Object> map);

    /**
     * 查询门诊号码、住院号码
     * @param map
     * @return
     */
    public Map<String,Object> queryCancelMzZyCode(Map<String,Object> map);

    /**
     * 检验标本采集通知
     * @param map
     * @return
     */
    public int insertLJytmxxBbzqCollect(Map<String,Object> map);

    /**
     * 检验标本采集通知
     * @param map
     * @return
     */
    public int updateLJytmxxCollect(Map<String,Object> map);

    /**
     * 更新L_JYTMXX ,取消检验标本采集通知
     * @param map
     * @return
     */
    public int updateLjytmxxCollectCancel(Map<String,Object> map);

    /**
     * 更新L_JYTMXX检验标本送检通知
     * @param map
     * @return
     */
    public int updateLabSampleSend(Map<String,Object> map);

    /**
     * 更新L_JYTMXX取消检验标本送检通知
     * @param map
     * @return
     */
    public int updateLabSampleSendCancel(Map<String,Object> map);

    /**
     * 更新l_jytmxx 用于标本接收
     * @param map
     * @return
     */
    public int updateLJYTMXX(Map<String,Object> map);

    /**
     * 标本周期监控,用于标准接收
     * @param map
     * @return
     */
    public int insertLJYTMXXBBZQ(Map<String,Object> map);

    /**
     *  更新l_jytmxx用于标本退回
     * @param map
     * @return
     */
    public int updateLjytmxx(Map<String,Object> map);

    /**
     * 更新l_testresult用于审核
     * @param map
     * @return
     */
    public int updateLtestresultAudit(Map<String,Object> map);

    /**
     * 更新l_jytmxx用于审核
     * @param map
     * @return
     */
    public int updateLjytmxxAudit(Map<String,Object> map);

    /**
     * 更新l_patientinfo用于审核
     * @param map
     * @return
     */
    public int updateLpatientinfoAudit(Map<String,Object> map);

    /**
     * 更新l_patientinfo用于取消审核
     * @param map
     * @return
     */
    public int updateLpatientinfoCancel(Map<String,Object> map);

    /**
     * 更新l_testresult用于取消审核
     * @param map
     * @return
     */
    public int updateLtestresultCancel(Map<String,Object> map);

    /**
     * 查询患者信息是否存在
     * @param map
     * @return
     */
    public int getLPatientInfo(Map<String,Object> map);

    /**
     * 查询检验结果表
     * @param map
     * @return
     */
    public int getLTestResult(Map<String,Object> map);
}
