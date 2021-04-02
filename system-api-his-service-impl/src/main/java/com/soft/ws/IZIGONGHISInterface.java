package com.soft.ws;

public interface IZIGONGHISInterface {

//    public String outPatientRegisteredQuery(String param);

    /**
     * 机构科室注册
     * @return
     */
    public String DepatmentAdd(String param) throws Exception;

    /**
     * 科室更新（同步）
     * @param param
     * @return
     */
    public String DepatmentUpdate(String param);

    /**
     * 机构人员注册
     * @param param
     * @return
     */
    public String EmployeeAdd(String param);

    /**
     * 机构人员更新（同步）
     * @param param
     * @return
     */
    public String EmployeeUpdate(String param);

    /**
     * 手术安排通知
     * @param param
     * @return
     */
    public String OptArrange(String param);

    /**
     * 取消手术安排通知
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
}
