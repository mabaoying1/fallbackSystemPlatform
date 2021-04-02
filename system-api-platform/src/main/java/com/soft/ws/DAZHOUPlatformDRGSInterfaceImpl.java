package com.soft.ws;

import com.soft.controller.DAZHOUDrgsController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 * @ClassName DAZHOUPlatformDRGSInterfaceImpl
 * @Description: TODO  达州采取的是HIS、手术提供视图，本程序负责抓取数据和封装</br>
 * @Author caidao
 * @Date 2020/10/19
 * @Version V1.0
 **/
public class DAZHOUPlatformDRGSInterfaceImpl implements IDAZHOUPlatformDRGSInterface{

    @Autowired
    private DAZHOUDrgsController dazhouDrgsController;

    @WebMethod(
            operationName="medicalServiceInterface", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    public String medicalServiceInterface(@WebParam(name="serviceId") String serviceId
            ,@WebParam(name="zyh") String zyh,@WebParam(name="zyhm") String zyhm, @WebParam(name="data") String data) {
        return dazhouDrgsController.drgsPackageUpload(serviceId,zyh,zyhm,data);
    }
}
