package ctd.net.rpc.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * 调用接口公共类
 * 
 * @ClassName: CallInterfaceUtil
 * @Description: TODO
 * @author: caidao
 * @date: 2019年10月17日 上午10:53:49
 */
@Slf4j
public class CallInterfaceUtil {

	/**
	 * 
	 * Description: <br>
	 * 
	 * @param WS_URL
	 *            接口地址 http://xxxxx?wsdl
	 * @param METHOD
	 *            调用的接口方法名
	 * @param Object[]
	 *            PARAMS 参数 {"OutPatientChargeDetails","","",""}
	 * @return
	 */
	public static Object call(String WS_URL, String METHOD, Object[] PARAMS) {
		log.info("调用平台call[start]>>>>>>>>>>>>>>>>>>>>>>>>>");
		log.info("CallInterfaceUtil[call]: ws_url[" + WS_URL + "],method[" + METHOD + "],params[" + PARAMS + "]");
		Object[] objects = null;
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		// QName opName = new
		// QName("http://impl.service.cqdp.custom.open.hrs.greenline.com", METHOD);
		org.apache.cxf.endpoint.Client client = dcf.createClient(WS_URL);
		try {
			// 设置超时时间
			HTTPConduit conduit = (HTTPConduit) client.getConduit();
			HTTPClientPolicy policy = new HTTPClientPolicy();
			long timeout = 5 * 60 * 1000;//
			policy.setConnectionTimeout(timeout);
			policy.setReceiveTimeout(timeout);
			conduit.setClient(policy);

			objects = client.invoke(METHOD, PARAMS);// , "XML"
			log.info("调用平台call[end]>>>>>>>>>>>>>>>>>>>>>>>>>");
			log.info("调用平台callReturn[" + objects[0] + "]>>>>>>>>>>>>>>>>>>>>>>>>>");
			return objects[0];
		} catch (Exception e) {
			log.error("调用接口地址：{}" + WS_URL, e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	* 此方法主要调用 asmx 类型接口
	* url 接口URL、
	* soapaction 域名、
	* params 接口参数Object[]数组、
	* method 接口方法名、
	* parameter 接口参数名（）List<String>集合----如无参数
	* 返回信息 Object
	 * @throws ServiceException 
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	* */
	public static Object webServiceAsmx(String url, String soapaction, Object[] params,
			String method, List<String> parameter) {
		Object obj = null;
		try {
			Service service = new Service();
			int i = 0;
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(url));
			call.setOperationName(new QName(soapaction, method)); // 设置要调用哪个方法
			for (; i < parameter.size(); i++) {
				call.addParameter(new QName(soapaction, parameter.get(i)),
						org.apache.axis.encoding.XMLType.XSD_STRING,
						javax.xml.rpc.ParameterMode.IN);
			}
			call.setUseSOAPAction(true);
			call.setSOAPActionURI(soapaction + method);
			if (i != 0) {//指定返回类型 根据ASMX返回类型指定
				//call.setReturnType(new QName(soapaction, method), Vector.class);
				call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);//（标准的类型）
				obj = call.invoke(params);
			} else {//无参数输入调用 根据ASMX返回类型指定 
				obj = call.invoke(params);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("调用接口地址：{webServiceAsmx}"+e.getMessage());
		}
		return obj;
	}
	 
}
