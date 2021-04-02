package ctd.net.rpc.util;

import org.apache.commons.lang3.StringUtils;
import java.util.Map;

/**
 * 字段非空验证类
 * @ClassName FieldVerifyEmptyUtil
 * @Description: TODO
 * @Author caidao
 * @Date 2021/3/23
 * @Version V1.0
 **/
public class FieldVerifyEmptyUtil {

    /**
     * 验证字段是否为空
     * @param paramMap 传入待验证的map <br>
     * @param excludeArray 排除不验证的字段 <br>
     * @return
     */
    public static String verifyEmpty(Map<String,Object> paramMap, String[] excludeArray){
        if(excludeArray != null && excludeArray.length != 0){
            for(String str : excludeArray){
                paramMap.remove(str);
            }
        }
        StringBuffer strBuff = new StringBuffer();
        int i = 0;
        for(Map.Entry entry : paramMap.entrySet()){
            if(StringUtils.isEmpty(entry.getValue().toString())){
                String mapKey = entry.getKey().toString();
                if(i == 0){
                    strBuff.append("参数不能为空,请检查参数 "+mapKey);
                }else{
                    strBuff.append("、"+mapKey);
                }
            }
        }
        return strBuff.toString();
    }
}
