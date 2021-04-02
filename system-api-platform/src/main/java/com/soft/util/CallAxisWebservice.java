package com.soft.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import java.rmi.RemoteException;
import java.util.Map;

/**
 * @ClassName CallAxisWebservice
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/20
 * @Version V1.0
 **/
@Component
public class CallAxisWebservice {

    private final Logger logger = LoggerFactory.getLogger(CallAxisWebservice.class);

    /**
     * 调用webservice接口
     * @param urlAddr 传入的wsdl全地址 <br>
     * @param nameSpaceURI 接口的命名空间<br>
     * @param method 接口方法名<br>
     * @param paramType 传入参数类型 string、boolean、double、float 、int、long、short、byte、anyType.....等等<br>
     * @param paramName 传入参数名与paramType顺序一一对应<br>
     * @param returnType 返回值类型设置<br>
     * @param paramValue 传入的参数值与参数名顺序一一对应
     * @return
     */
    public  String callAxisService(String urlAddr,String nameSpaceURI
            , String method, String[] paramType, String[] paramName , String returnType
            ,Object[] paramValue) {
        String uuid = UUIDUtil.getUUID();
        logger.info("数据标识["+uuid+"];最终入参[paramValue]->["+paramValue+"],[submitUrl]->["+urlAddr+"]");
        // 远程调用路径
//        String endpoint =  "http://172.30.51.115:7232/drgs?wsdl";
        String result = "call failed!";
        Service service = new Service();
        Call call;
        try {
            call = (Call)service.createCall();
            call.setTargetEndpointAddress(urlAddr); //url路径地址*****?wsdl
            //new QName的URL是要指向的命名空间的名称，这个URL地址在你的wsdl打开后可以看到的，
            //上面有写着targetNamespace="http://*.*.*/",这个就是你的命名空间值了;
            //"http://www.bsoft.drgs.com" //operationRequest
            call.setOperationName(new QName(nameSpaceURI,method)); // 调用的方法名
            // 设置参数名 :参数名 ,参数类型:String, 参数模式：'IN' or 'OUT'
            setParamTypeAndName(call, paramType, paramName);
//            call.addParameter("zyh", XMLType.XSD_STRING, ParameterMode.IN);
//            call.addParameter("serviceID", XMLType.XSD_STRING, ParameterMode.IN);
            call.setEncodingStyle("UTF-8");
            setReturnType(call,returnType);
//            call.setReturnType(XMLType.XSD_STRING);
            //new Object[] {"IP0000147484","ba"}
            result = call.invoke(paramValue).toString();// 远程调用
            logger.info("数据标识["+uuid+"];接口返回：["+result+"]");
        }  catch (RemoteException e) {
            e.printStackTrace();
            logger.error("数据标识["+uuid+"];callHttpPost异常：["+e.getMessage()+"]");
            return JSONObject.toJSONString(ResultMessage.error(e.getMessage()));
        } catch (javax.xml.rpc.ServiceException e) {
            e.printStackTrace();
            logger.error("数据标识["+uuid+"];callHttpPost异常：["+e.getMessage()+"]");
            return JSONObject.toJSONString(ResultMessage.error(e.getMessage()));
        }
        return result;
    }

    /**
     * 根据传入的参数类型，参数名称
     * @param call
     * @param paramType 传入参数类型 <br>
     * @param paramName 传入参数名称 <br>
     */
    public void setParamTypeAndName(Call call , String[] paramType, String[] paramName){
        for (int i = 0; i < paramType.length ; i++){
            switch (paramType[i]){
                case "string" :
                    call.addParameter(paramName[i], XMLType.XSD_STRING, ParameterMode.IN);
                    break;
                case "boolean" :
                    call.addParameter(paramName[i], XMLType.XSD_BOOLEAN, ParameterMode.IN);
                    break;
                case "double" :
                    call.addParameter(paramName[i], XMLType.XSD_DOUBLE, ParameterMode.IN);
                    break;
                case "float" :
                    call.addParameter(paramName[i], XMLType.XSD_FLOAT, ParameterMode.IN);
                    break;
                case "int" :
                    call.addParameter(paramName[i], XMLType.XSD_INT, ParameterMode.IN);
                    break;
                case "integer" :
                    call.addParameter(paramName[i], XMLType.XSD_INTEGER, ParameterMode.IN);
                    break;
                case "long" :
                    call.addParameter(paramName[i], XMLType.XSD_LONG, ParameterMode.IN);
                    break;
                case "short" :
                    call.addParameter(paramName[i], XMLType.XSD_SHORT, ParameterMode.IN);
                    break;
                case "byte" :
                    call.addParameter(paramName[i], XMLType.XSD_BYTE, ParameterMode.IN);
                    break;
                case "decimal" :
                    call.addParameter(paramName[i], XMLType.XSD_DECIMAL, ParameterMode.IN);
                    break;
                case "base64Binary" :
                    call.addParameter(paramName[i], XMLType.XSD_BASE64, ParameterMode.IN);
                    break;
                case "hexBinary" :
                    call.addParameter(paramName[i], XMLType.XSD_HEXBIN, ParameterMode.IN);
                    break;
                case "dateTime" :
                    call.addParameter(paramName[i], XMLType.XSD_DATETIME, ParameterMode.IN);
                    break;
                case "time" :
                    call.addParameter(paramName[i], XMLType.XSD_TIME, ParameterMode.IN);
                    break;
                case "date" :
                    call.addParameter(paramName[i], XMLType.XSD_DATE, ParameterMode.IN);
                    break;
                case "anyType" :
                    call.addParameter(paramName[i], XMLType.XSD_ANYTYPE, ParameterMode.IN);
                    break;
                case "any" :
                    call.addParameter(paramName[i], XMLType.XSD_ANY, ParameterMode.IN);
                    break;
                case "QName" :
                    call.addParameter(paramName[i], XMLType.XSD_QNAME, ParameterMode.IN);
                    break;
                case "anySimpleType" :
                    call.addParameter(paramName[i], XMLType.XSD_ANYSIMPLETYPE, ParameterMode.IN);
                    break;
            }
        }
    }

    /**
     * 设置返回值类型
     * @param call
     * @param returnType
     */
    public void setReturnType(Call call , String returnType){
            switch (returnType){
                case "string" :
                    call.setReturnType(XMLType.XSD_STRING);
                    break;
                case "boolean" :
                    call.setReturnType(XMLType.XSD_BOOLEAN);
                    break;
                case "double" :
                    call.setReturnType(XMLType.XSD_DOUBLE);
                    break;
                case "float" :
                    call.setReturnType(XMLType.XSD_FLOAT);
                    break;
                case "int" :
                    call.setReturnType(XMLType.XSD_INT);
                    break;
                case "integer" :
                    call.setReturnType(XMLType.XSD_INTEGER);
                    break;
                case "long" :
                    call.setReturnType(XMLType.XSD_LONG);
                    break;
                case "short" :
                    call.setReturnType(XMLType.XSD_SHORT);
                    break;
                case "byte" :
                    call.setReturnType(XMLType.XSD_BYTE);
                    break;
                case "decimal" :
                    call.setReturnType(XMLType.XSD_DECIMAL);
                    break;
                case "base64Binary" :
                    call.setReturnType(XMLType.XSD_BASE64);
                    break;
                case "hexBinary" :
                    call.setReturnType(XMLType.XSD_HEXBIN);
                    break;
                case "dateTime" :
                    call.setReturnType(XMLType.XSD_DATETIME);
                    break;
                case "time" :
                    call.setReturnType(XMLType.XSD_TIME);
                    break;
                case "date" :
                    call.setReturnType(XMLType.XSD_DATE);
                    break;
                case "anyType" :
                    call.setReturnType(XMLType.XSD_ANYTYPE);
                    break;
                case "any" :
                    call.setReturnType(XMLType.XSD_ANY);
                    break;
                case "QName" :
                    call.setReturnType(XMLType.XSD_QNAME);
                    break;
                case "anySimpleType" :
                    call.setReturnType(XMLType.XSD_ANYSIMPLETYPE);
                    break;
        }
    }
}
