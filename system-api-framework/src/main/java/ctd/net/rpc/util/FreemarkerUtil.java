package ctd.net.rpc.util;

import java.io.IOException;
import java.io.StringWriter;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
  
/**
 * freemarker 模版
 * @author caidao
 *
 */
@Slf4j
@SuppressWarnings("deprecation")
public class FreemarkerUtil {
	
	public static final String CHAR_TEMPLATE_PATH="/";
	public static Configuration char_cfg;
	/**
	 * 初始化freemarker 模板配置器
	 */
	static
	{
		log.debug("初始化报表freemarker模板配置 模板地址：{}",CHAR_TEMPLATE_PATH);
		char_cfg = new Configuration();
		char_cfg.setTemplateLoader(new ClassTemplateLoader(FreemarkerUtil.class, CHAR_TEMPLATE_PATH));
		char_cfg.setClassicCompatible(true);	//处理空值为空字符串
	}
	
	/**
     * 
     * Description:  转换xml 默认模板地址是 classpath
     * @param xml 模板名字
     * @param data 数据
     * @return 找不到模板返回null
     */
	public static String castXML(String xml ,Object data)
	{
		if(StringUtil.isEmpty(xml))
		{
			log.error("生成报表xml 传入xml文件名为空");
			return null;
		}
		StringWriter writer = new StringWriter();
		try
		{
			Template template = char_cfg.getTemplate(xml, "utf-8");
	        template.process(data, writer);
	        writer.flush();
		} catch (IOException e) 
		{
			log.error("生成报表freemark：{}；参数：{},"+"【"+CHAR_TEMPLATE_PATH+xml+"】;"+data);
			e.printStackTrace();
			//			throw e;
		}catch (TemplateException e)
		{
			log.error("生成报表freemark：{}；参数：{},"+"【"+CHAR_TEMPLATE_PATH+xml+"】;"+data);
			e.printStackTrace();
			//			throw e;
		}finally{
			 try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		return writer.toString();
	}
}
