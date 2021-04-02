package com.soft.ws;

public interface IZIGONGLISInterface {

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

}
