package com.soft.ws;

/**
 * 〈迪安一般检验结果描述〉<br>
 *
 * @className: JbzyyDiAnInerface
 * @package: com.soft.ws
 * @author: Ljx
 * @date: 2021/03/19 14:58
 */
public interface JbzyyDiAnInerface {
    /**
     * (一)	机构认证注册接口
     * @return
     */
    public String DiAnInspection(String url,String ClientID,String ClientGUID,String method);
    /**
     * 概述
     *
     * @author Ljx
     * @date 2021/03/22 11:05
     * @param	url	,ClientID,ClientGUID,method
     * @return
     */
    public String DiAnMicrobial(String url,String ClientID,String ClientGUID,String method);

}
