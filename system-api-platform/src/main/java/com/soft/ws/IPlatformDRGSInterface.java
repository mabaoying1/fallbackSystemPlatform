package com.soft.ws;

/**
 * @ClassName IPlatformInterface
 * @Description: TODO
 * @Author caidao
 * @Date 2020/9/28
 * @Version V1.0
 **/
public interface IPlatformDRGSInterface {

    /**
     * (一)	机构认证注册接口
     * @return
     */
    public String orgRegistv1(String param);

    /**
     * (二)	机构认证注册修改接口
     * @param param
     * @return
     */
    public String orgRegistv2(String param);

    /**
     * (三)	医院信息接口
     * @param param
     * @return
     */
    public String hospitalv1(String param);

    /**
     * (四)	医院信息修改接口
     * @param param
     * @return
     */
    public String hospitalv2(String param);

    /**
     * (五)	住院病案首页接口
     * @param param
     * @return
     */
    public String baInfov1(String param);

    /**
     * (六)	住院病案首页冲销接口
     * @param param
     * @return
     */
    public String baInfov2(String param);

    /**
     * (七)	科室匹配接口
     * @param param
     * @return
     */
    public String departmentMatchv1(String param);

    /**
     * (八)	科室匹配修改接口
     * @param param
     * @return
     */
    public String departmentMatchv2(String param);

    /**
     * (九)	标准科室查询接口
     * @param param
     * @return
     */
    public String standDepartmentv1(String param);

    /**
     * (十)	病案质控反馈查询接口
     * @param param
     * @return
     */
    public String lczkv2(String param);

    /**
     * (十一)	分组结果查询接口
     * @param param
     * @return
     */
    public String drgResultv1(String param);

    /**
     * (十二)	住院费用结算清单接口
     * @param param
     * @return
     */
    public String medicalFeeInfov1(String param);

    /**
     * (十三)	住院费用结算清单冲销接口
     * @param param
     * @return
     */
    public String medicalFeeInfov2(String param);

    /**
     * (十四)	住院诊疗费用明细接口
     * @param param
     * @return
     */
    public String detailsv1(String param);

    /**
     * (十五)	住院诊疗费用明细冲销接口
     * @param param
     * @return
     */
    public String detailsv2(String param);
}
