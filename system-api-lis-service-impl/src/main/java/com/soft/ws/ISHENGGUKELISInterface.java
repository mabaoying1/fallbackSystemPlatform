package com.soft.ws;

public interface ISHENGGUKELISInterface {


    /**
     * 检验住院计费
     * @param param
     * @return
     */
    public String LabRequestInHospitalPayBill(String param);

    /**
     * 检验住院取消计费
     * @param param
     * @return
     */
    public String LabRequestInHospitalReturnBill(String param);

    /**
     * 检验条码打印通知
     * @return
     */
    public String LabBarcodeCreate(String param);

    /**
     * 取消检验条码打印通知
     * @param param
     * @return
     */
    public String LabBarcodeCreateCancel(String param);

    /**
     * 检验标本采集通知
     * @param param
     * @return
     */
    public String LabSampleCollect(String param);

    /**
     * 取消检验标本采集通知
     * @param param
     * @return
     */
    public String LabSampleCollectCancel(String param);

    /**
     * 检验标本送检通知
     * @param param
     * @return
     */
    public String LabSampleSend(String param);

    /**
     * 取消检验标本送检通知
     * @param param
     * @return
     */
    public String LabSampleSendCancel(String param);

    /**
     * 检验标本核收通知
     * @param param
     * @return
     */
    public String LabSampleReceive(String param);

    /**
     * 检验标本退回通知
     * @param param
     * @return
     */
    public String LabSampleReturn(String param);

    /**
     * 检验报告审核通知
     * @param param
     * @return
     */
    public String LabReportAudit(String param);

    /**
     * 取消检验报告审核通知
     * @param param
     * @return
     */
    public String LabReportAuditCancel(String param);

}
