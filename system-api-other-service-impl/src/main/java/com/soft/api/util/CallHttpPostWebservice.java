package com.soft.api.util;
import com.alibaba.fastjson.JSONObject;
import ctd.net.rpc.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * 通过post提交
 * @ClassName CallHttpPostWebservice
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/19
 * @Version V1.0
 **/
@Component
public class CallHttpPostWebservice {

    private final Logger logger = LoggerFactory.getLogger(CallHttpPostWebservice.class);

    /**
     * 通过POST方式调用
     * @param inParam 需要传递的参数 MultiValueMap<String, Object> </br>
     * @param submitUrl 访问的URL地址 </br>
     * @param paramType 默认"application/json; charset=UTF-8" </br>
     * @return JSON字符串
     */
    public String callHttpPost(MultiValueMap<String, Object> inParam, String submitUrl, String paramType) {
        String uuid = UUIDUtil.getUUID();
        logger.info("httpPost标识["+uuid+"];最终入参[inParam]->["+inParam+"],[submitUrl]->["+submitUrl+"]");
        String resStr = "";
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            headers.set("Accept", paramType == null || paramType.isEmpty() ? "application/json; charset=UTF-8" : paramType);
            //将请求头部和参数合成一个请求
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(inParam, headers);
            ResponseEntity<String> resp = restTemplate.exchange(submitUrl, HttpMethod.POST, requestEntity, String.class);
            resStr = resp.getBody();
            JSONObject jsons =JSONObject.parseObject(resStr);
            logger.info("httpPost标识["+uuid+"];接口返回：["+jsons+"]");
            return jsons.toString();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("httpPost标识["+uuid+"];callHttpPost异常：["+e.getMessage()+"]");
            return JSONObject.toJSONString(ResultMessage.error(e.getMessage()));
        }
    }


}
