package com.soft.ws;

import com.soft.Entity.DrgsParameterEntity;
import com.soft.controller.DrgsController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @ClassName PlatformInterfaceImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/9/28
 * @Version V1.0
 **/
@WebService(
        targetNamespace="http://ws.soft.com", //命名空间,一般是接口的包名
        name="IPlatformDRGSInterface",
        serviceName="Drgs"   //该webservice服务的名称
)
@Component
public class PlatformDRGSInterfaceImpl implements IPlatformDRGSInterface {

    @Autowired
    private DrgsController drgsController;

    @Autowired
    private DrgsParameterEntity drgsParameterEntity;


    @WebMethod(
            operationName="orgRegistv1", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    //511600-H1001-208984
    //(一)	机构认证注册接口
    @Override
    public String orgRegistv1(@WebParam(name="param") String param) {
        String submitUrl = "http://"+drgsParameterEntity.getIpAddr()+"/drgPms/nologin/drgUpload/orgRegist/v1";
        String resultStr = drgsController.callDrugs(param,submitUrl);
        return resultStr;
    }

    @WebMethod(
            operationName="orgRegistv2", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    //(二)	机构认证注册修改接口
    public String orgRegistv2(@WebParam(name="param") String param) {
        String submitUrl = "http://"+drgsParameterEntity.getIpAddr()+"/drgPms/nologin/drgUpload/orgRegist/v2";
        String resultStr = drgsController.callDrugs(param,submitUrl);
        return resultStr;
    }

    @WebMethod(
            operationName="hospitalv1", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    //(三)	医院信息接口  []
    public String hospitalv1(@WebParam(name="param") String param) {
        String submitUrl = "http://"+drgsParameterEntity.getIpAddr()+"/drgPms/nologin/drgUpload/hospital/v1";
        String resultStr = drgsController.callDrugs(param,submitUrl,true);
        return resultStr;
    }

    @WebMethod(
            operationName="hospitalv2", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    //(四)	医院信息修改接口 []
    @Override
    public String hospitalv2(@WebParam(name="param")String param) {
        String submitUrl = "http://"+drgsParameterEntity.getIpAddr()+"/drgPms/nologin/drgUpload/hospital/v2";
        String resultStr = drgsController.callDrugs(param,submitUrl,true);
        return resultStr;
    }

    @WebMethod(
            operationName="baInfov1", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    //(五)	住院病案首页接口
    public String baInfov1(@WebParam(name="param")String param) {
        String submitUrl = "http://"+drgsParameterEntity.getIpAddr()+"/drgPms/nologin/drgUpload/baInfo/v1";
        String resultStr = drgsController.callDrugs(param,submitUrl);
        return resultStr;
    }

    @WebMethod(
            operationName="baInfov2", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    //(六)	住院病案首页冲销接口
    public String baInfov2(@WebParam(name="param")String param) {
        String submitUrl = "http://"+drgsParameterEntity.getIpAddr()+"/drgPms/nologin/drgUpload/baInfo/v2";
        String resultStr = drgsController.callDrugs(param,submitUrl);
        return resultStr;
    }

    @WebMethod(
            operationName="departmentMatchv1", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    //(七)	科室匹配接口 []
    public String departmentMatchv1(@WebParam(name="param") String param) {
        String submitUrl = "http://"+drgsParameterEntity.getIpAddr()+"/drgPms/nologin/drgUpload/departmentMatch/v1";
        String resultStr = drgsController.callDrugs(param,submitUrl,true);
        return resultStr;
    }

    @WebMethod(
            operationName="departmentMatchv2", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    //(八)	科室匹配修改接口 []
    public String departmentMatchv2(@WebParam(name="param") String param) {
        String submitUrl = "http://"+drgsParameterEntity.getIpAddr()+"/drgPms/nologin/drgUpload/departmentMatch/v2";
        String resultStr = drgsController.callDrugs(param,submitUrl,true);
        return resultStr;
    }


    @WebMethod(
            operationName="standDepartmentv1", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    //(九)	标准科室查询接口
    public String standDepartmentv1(@WebParam(name="param") String param) {
        String submitUrl = "http://"+drgsParameterEntity.getIpAddr()+"/drgPms/nologin/drgUpload/standDepartment/v1";
        String resultStr = drgsController.callDrugs(param,submitUrl);
        return resultStr;
    }

    @WebMethod(
            operationName="lczkv2", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    //(十)	病案质控反馈查询接口
    public String lczkv2(@WebParam(name="param") String param) {
        String submitUrl = "http://"+drgsParameterEntity.getIpAddr()+"/drgPms/nologin/drgUpload/lczk/v2";
        String resultStr = drgsController.callDrugs(param,submitUrl);
        return resultStr;
    }

    @WebMethod(
            operationName="drgResultv1", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    //(十一)	分组结果查询接口
    public String drgResultv1(@WebParam(name="param") String param) {
        String submitUrl = "http://"+drgsParameterEntity.getIpAddr()+"/drgPms/nologin/drgUpload/drgResult/v1";
        String resultStr = drgsController.callDrugs(param,submitUrl);
        return resultStr;
    }

    @WebMethod(
            operationName="medicalFeeInfov1", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    //(十二)	住院费用结算清单接口
    public String medicalFeeInfov1(@WebParam(name="param") String param) {
        String submitUrl = "http://"+drgsParameterEntity.getIpAddr()+"/drgPms/nologin/drgUpload/medicalFeeInfo/v1";
        String resultStr = drgsController.callDrugs(param,submitUrl);
        return resultStr;
    }

    @WebMethod(
            operationName="medicalFeeInfov2", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    @Override
    //(十三)	住院费用结算清单冲销接口
    public String medicalFeeInfov2(@WebParam(name="param") String param) {
        String submitUrl = "http://"+drgsParameterEntity.getIpAddr()+"/drgPms/nologin/drgUpload/medicalFeeInfo/v2";
        String resultStr = drgsController.callDrugs(param,submitUrl);
        return resultStr;
    }

    @WebMethod(
            operationName="detailsv1", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    //(十四)	住院诊疗费用明细接口 []
    @Override
    public String detailsv1(@WebParam(name="param") String param) {
        String submitUrl = "http://"+drgsParameterEntity.getIpAddr2()+"/drgPms/nologin/drgUpload/details/v1";
        String resultStr = drgsController.callDrugs(param,submitUrl);
        return resultStr;
    }

    @WebMethod(
            operationName="detailsv2", //发布的方法名称
            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法
    )
    //(十五)	住院诊疗费用明细冲销接口
    @Override
    public String detailsv2(@WebParam(name="param") String param) {
        String submitUrl = "http://"+drgsParameterEntity.getIpAddr2()+"/drgPms/nologin/drgUpload/details/v2";
        String resultStr = drgsController.callDrugs(param,submitUrl);
        return resultStr;
    }
}
