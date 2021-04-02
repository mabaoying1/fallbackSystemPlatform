package com.soft.ws;


import com.soft.controller.JbzyyDiAnController;
import com.soft.controller.JbzyyDiAnMicrobialController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * 〈迪安一般检验结果描述〉<br>
 *
 * @className: JbzyyDiAnInterface
 * @package: com.soft.ws
 * @author: Ljx
 * @date: 2021/03/19 14:50
 */
@WebService(
        targetNamespace="http://ws.soft.com", //命名空间,一般是接口的包名
        name="JbzyyDiAnInterface",
        serviceName="call"   //该webservice服务的名称
)
@Component
public class JbzyyDiAnImpl implements JbzyyDiAnInerface {
    @Autowired
    private JbzyyDiAnController jbzyyDiAnController;
    @Autowired
    private JbzyyDiAnMicrobialController jbzyyDiAnMicrobialController;

    @WebMethod(
            operationName="DiAnInspection", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String DiAnInspection(@WebParam(name="url") String url
            ,@WebParam(name="ClientID") String ClientID,@WebParam(name="ClientGUID") String ClientGUID, @WebParam(name="method") String method) {
        return jbzyyDiAnController.DiAnInspection(url,ClientID,ClientGUID,method);
    }

    @WebMethod(
            operationName="DiAnMicrobial", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String DiAnMicrobial(@WebParam(name="url") String url
            ,@WebParam(name="ClientID") String ClientID,@WebParam(name="ClientGUID") String ClientGUID, @WebParam(name="method") String method) {
        return jbzyyDiAnMicrobialController.DiAnMicrobial(url,ClientID,ClientGUID,method);
    }
}
