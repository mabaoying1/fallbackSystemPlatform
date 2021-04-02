package com.soft.ws;

public interface ISCPublicInterface {

    /**
     * 查询业务系统患者信息
     * @param param
     * @return
     */
    public String QueryPatientInfo(String param);
    
    /**
     * 查询业务系统患者住院记录
     * @param param
     * @return
     */
    public String QueryInhospitalInfo(String param);


    /**
     * 查询住院患者陪护者信息
     * @param param
     * @return
     */
    public String QueryPatientChaperone(String param);

    /**
     * 查询住院患者陪护者信息
     * @param param
     * @return
     */
    public String QueryPatientChaperoneNucleateResult(String param);
    /**
     * 查询预约住院病人信息
     * @param param
     * @return
     */
    public String QueryPatientOrderInhospital(String param);


    /**
     * 查询医疗收费目录信息
     * @param param
     * @return
     */
    public String QueryFeeCatalogInfo(String param);

    /**
     * 住院补计费用（zy_fymx）
     * @param param
     * @return
     */
    public String InhospitalFillUpFee(String param);

    /**
     * 住院作废补计费用
     * @param param
     * @return
     */
    public String InhospitalCancelFillUpFee(String param);
}
