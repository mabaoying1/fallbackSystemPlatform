package ctd.net.rpc.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import java.util.Map;

/**
 * @ClassName ObjectToMap
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/27
 * @Version V1.0
 **/
@Slf4j
public class ObjectToMap {

    /**
     * 把参数object 转成 map
     * @param object 可以是JSON、XML
     * @return Map<String, Object>
     * @throws DocumentException
     */
    public static Map<String,Object> getObjectToMap(String object) throws DocumentException {
        if(isXML(object)){
            return XmlToMap.xml2mapWithAttr(object,false);
        }else{
            Map maps = (Map) JSON.parse(object);
            return maps;
        }
    }

    /**
     * 判断入参类型JSON/XML
     * @param object
     * @return
     * @throws DocumentException
     */
    public static String getObjectType(String object) throws DocumentException {
        if(isXML(object)){
            return "XML";
        }else{
            return "JSON";
        }
    }

    /**
     * 判断是否是xml结构
     */
    public static boolean isXML(String value) {
        try {
            DocumentHelper.parseText(value);
        } catch (DocumentException e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage());
            return false;
        }
        return true;
    }


    public static String isXmlORJson(String content) {
        if(StringUtils.isEmpty(content)){
            return "";
        }
        boolean isJsonObject = true;
        boolean isJsonArray = true;
        try {
            JSONObject.parseObject(content);
        } catch (Exception e) {
            isJsonObject = false;
        }
        try {
            JSONObject.parseArray(content);
        } catch (Exception e) {
            isJsonArray = false;
        }
        if(!isJsonObject && !isJsonArray){ //不是json格式
            try {
                DocumentHelper.parseText(content);
                return "XML";
            } catch (DocumentException e) {
                return "";
            }
        }
        return "JSON";
    }

}
