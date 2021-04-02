package com.soft.ws;

import javax.jws.WebMethod;

/**
 * 达州DRGS暴露接口
 */
public interface IDAZHOUPlatformDRGSInterface {

    public String medicalServiceInterface(String serviceId,String zyh,String zyhm,String data);
}
