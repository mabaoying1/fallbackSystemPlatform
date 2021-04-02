package com.soft.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soft.util.ElasticsearchUtil;
import com.soft.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.*;

/**
 * 处理成DRGS格式，调用DRGS接口
 * @ClassName DrgsController
 * @Description: TODO
 * @Author caidao
 * @Date 2020/9/28
 * @Version V1.0
 **/

@RestController
public class DrgsController {


    @Autowired
    private ElasticsearchUtil elasticsearchUtil;

    private final Logger logger = LoggerFactory.getLogger(DrgsController.class);

    public String callDrugs(String inParam, String submitUrl){
        return callDrugs(inParam,submitUrl,false);
    }

    //SpringBoot RestTemplate发送Post请求
    public String callDrugs(String inParam, String submitUrl, boolean moreJson) {
        logger.info("callDrugs.............start");
        String uuid = UUIDUtil.getUUID();
        logger.info("数据标识【"+uuid+"】,原始入参[inParam]->["+inParam+"],[submitUrl]->["+submitUrl+"]");
//        elasticsearchUtil.addElasticsearchData((Map)JSON.parse(inParam),"log","interface",UUIDUtil.getUUID());
 //        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
        String resStr = "";
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            headers.set("Accept", "application/json; charset=UTF-8");
            JSONObject jsonsIn =JSONObject.parseObject(inParam);
//            param.add("APP_ID", jsonsIn.get("appID").toString());
//            param.add("ORG_CODE", jsonsIn.get("orgCode").toString());
//            jsonsIn.remove("appID");
//            jsonsIn.remove("orgCode");
            jsonLoop(jsonsIn,param,moreJson);
            logger.info("数据标识【"+uuid+"】,封装完参数->"+param);
//            param.add("MED_FEE_INFO", jsonsIn.toString());
            //将请求头部和参数合成一个请求
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(param, headers);
            ResponseEntity<String> resp = restTemplate.exchange(submitUrl, HttpMethod.POST, requestEntity, String.class);
            resStr = resp.getBody();
            JSONObject jsons =JSONObject.parseObject(resStr);
//            request.setAttribute(LoggerUtil.LOGGER_RETURN,jsons);
            logger.info("数据标识【"+uuid+"】,DRGS返回："+jsons);
            return jsons.toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("数据标识【"+uuid+"】,callDrugs异常：["+e.getMessage()+"]");
            return "{\"success\":\"false\",\"errorCode\":\"500\",\"errorMsg\":\""+e.getMessage()+"\"}";
        }
    }

    public String callDrugsByOrg(String inParam, String submitUrl){
        return callDrugsByOrg(inParam,submitUrl,false);
    }

    /**
     * 注册机构
     * @param inParam
     * @param submitUrl
     * @return
     */
    public String callDrugsByOrg(String inParam, String submitUrl,boolean moreJosn) {
        logger.info("callDrugsByOrg.............start");
        String uuid = UUIDUtil.getUUID();
        logger.info("数据标识【"+uuid+"】,原始入参[inParam]->["+inParam+"],[submitUrl]->["+submitUrl+"]");
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
        MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
        String resStr = "";
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            headers.set("Accept", "application/json; charset=UTF-8");
//            param.add("MED_FEE_INFO", inParam);
            JSONObject jsonsIn =JSONObject.parseObject(inParam);
            jsonLoop(jsonsIn,param,moreJosn);
            logger.info("数据标识【"+uuid+"】,封装完参数->"+param);
            //将请求头部和参数合成一个请求
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(param, headers);
            ResponseEntity<String> resp = restTemplate.exchange(submitUrl, HttpMethod.POST, requestEntity, String.class);
            resStr = resp.getBody();
            JSONObject jsons =JSONObject.parseObject(resStr);
            logger.info("数据标识【"+uuid+"】,DRGS返回："+jsons);
//            request.setAttribute(LoggerUtil.LOGGER_RETURN,jsons);
            return jsons.toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("数据标识【"+uuid+"】,callDrugsByOrg异常：["+e.getMessage()+"]");
            return "{\"success\":\"false\",\"errorCode\":\"500\",\"errorMsg\":\""+e.getMessage()+"\"}";
        }
    }

    /**
     * json格式转换成DRGS参数格式
     * @param object
     * @param param
     */
    public void jsonLoop(Object object ,MultiValueMap<String, Object> param,boolean moreJson) {
        JSONObject jsonObject = (JSONObject) object;
        Iterator iter = jsonObject.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if(entry.getValue() instanceof JSON){
                if(moreJson){
                    param.set(entry.getKey().toString(),"["+((JSON) entry.getValue()).toString()+"]");
                    continue;
                }
            }
            param.set(entry.getKey().toString(),entry.getValue().toString());
        }
    }
}
