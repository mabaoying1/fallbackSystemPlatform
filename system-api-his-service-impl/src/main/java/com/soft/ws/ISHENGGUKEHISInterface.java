package com.soft.ws;

/**
 * @ClassName ISHENGGUKEHISInterface
 * @Description: TODO
 * @Author caidao
 * @Date 2020/11/24
 * @Version V1.0
 **/
public interface ISHENGGUKEHISInterface {

    /**
     * 查询医疗收费
     * @param param
     * @return
     */
    public String QuerySfxm(String param);

    /**
     * 查询医疗项目
     * @param param
     * @return
     */
    public String QueryMedicalProject(String param);

    /**
     * 输血退费
     * @param param
     * @return
     */
    public String TransfusionReturnBill(String param);

    /**
     * 输血计费
     * @param param
     * @return
     */
    public String TransfusionPayBill(String param);

    /**
     * 输血医嘱作废撤消
     * @param param
     * @return
     */
    public String TransfusionDoctorAdviceCancel(String param);

    /**
     * 输血医嘱
     * @param param
     * @return
     */
    public String TransfusionDoctorAdvice(String param);

    /**
     * 患者基本信息注册
     * @param param
     * @return
     */
    public String PatientRegistry(String param);

    /**
     * 患者基本信息更新
     * @param param
     * @return
     */
    public String PatientUpdate(String param);

    /**
     * 手术安排
     * @param param
     * @return
     */
    public String OptArrange(String param);

    /**
     * 取消手术安排
     * @param param
     * @return
     */
    public String OptArrangeCancel(String param);

    /**
     * 手术完成通知
     * @param param
     * @return
     */
    public String OptComplete(String param);

    /**
     * 取消手术完成通知
     * @param param
     * @return
     */
    public String OptCompleteCancel(String param);

    /**
     * 检查登记执行通知
     * @param param
     * @return
     */
    public String ExmRequestExecuted(String param);

    /**
     * 取消检查登记执行通知
     * @param param
     * @return
     */
    public String ExmRequestExecutedCancel(String param);

    /**
     * 检查报告审核
     * @param param
     * @return
     */
    public String ExmReportAudit(String param);

    /**
     * 检查报告取消审核
     * @param param
     * @return
     */
    public String ExmReportAuditCancel(String param);

    /**
     * 检查/检验报告上传
     * @param param
     * @return
     */
    public String EMRDocumentRegistry(String param);

}
