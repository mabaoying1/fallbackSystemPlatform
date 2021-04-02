package ctd.net.rpc.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import sun.misc.BASE64Decoder;

/**
 * 字符串工具类
 * 
 * @author caidao
 *
 */
public class StringUtil {

	private StringUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 判断空 null 和 "" 都返回 true
	 * 
	 * @param str
	 * @return true 空的
	 */
	public static boolean isEmpty(String str) {

		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;

	}

	
	/**
	 * 判断空 null 和 "" 都返回 true
	 * @param sts  字符串数组
	 * @return
	 */
	
	public static boolean isEmpty(String... sts) {

		if (sts == null || sts.length == 0) {
			return true;
		}
		for (String str : sts) {
			if (isEmpty(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将传入的字符串转换成BigDecimal , 如果传入空值, 则返回null
	 * @param value
	 * @return
	 */
	public static BigDecimal castToBigDecimal(String value) {

		return isEmpty(value) ? null : new BigDecimal(value);
	}
	
	/**
	 * 将传入的字符串转换成Double , 如果传入空值, 则返回null
	 * @param value
	 * @return
	 */
	public static Double castToDouble(String value) {

		return isEmpty(value) ? null : Double.parseDouble(value);
	}

	/**
	 * 将传入的字符串转换成Long , 传 null或者 null 默认返回 0
	 * 
	 * @param value
	 * @return
	 */
	public static Long castTolong(String value) {
		
		return isEmpty(value) ? 0 : Long.parseLong(value);
	}
	
	/**
	 * 将传入的字符串BASE64加密
	 * @param str
	 * @return
	 */
	public static String base64Encoding(String str) {
		if (str == null)
            return null;
        String res = "";
        try {
            res = new sun.misc.BASE64Encoder().encode(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return res;
	}

	/**
	 * 解密base64
	 * @param s
	 * @return
	 */
	public static String base64Decoding(String s) {
		byte[] b = null;
		String result = null;
		if (s != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(s);
				result = new String(b,"UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
