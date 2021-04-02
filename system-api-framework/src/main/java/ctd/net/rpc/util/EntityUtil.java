package ctd.net.rpc.util;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 实体转换工具
 * @author caidao
 *
 */
public class EntityUtil {
	
	private static ObjectMapper objectMapper;
	static
	{  
		objectMapper = new ObjectMapper();
 
	}
	private EntityUtil()
	{
		
	}
	/**
	 * 将对象序列化转换成json格式字符串
	 * @param obj
	 * @return
	 * @throws JsonProcessingException 
	 */
	public static String castToJsonString(Object obj) throws JsonProcessingException
	{
		return objectMapper.writeValueAsString(obj);
	}
	
	/**
	 * 
	 * Description:将异常信息序列化成json <br>
	 * @param exception
	 * @return
	 * @throws JsonProcessingException 
	 */
	public static String castExceptionToJson(Exception exception) throws JsonProcessingException
	{
		return castToJsonString(exception.getMessage());
	}
	
	
	/**
	 * 将json形式字符串转换为java实体类
	 * @param jsonStr 
	 * @param clazz 类.class
	 * @return
	 */
	public static <T> T parse(String jsonStr, Class<T> clazz) {
	    ObjectMapper om = new ObjectMapper();
	    T readValue = null;
	    try {
	        readValue = om.readValue(jsonStr, clazz);
	    } catch (JsonParseException e) {
	        e.printStackTrace();
	    } catch (JsonMappingException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return readValue;
	}

}
