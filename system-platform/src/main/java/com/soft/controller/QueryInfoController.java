package com.soft.controller;

import com.soft.SystemPlatformAppRun;
import com.soft.api.util.FreemarkerUtilNew;
//import com.soft.util.ElasticsearchUtil;
//import com.soft.util.FreemarkerUtilNew;
import ctd.net.rpc.util.ElasticsearchUtil;
//import ctd.net.rpc.util.FileSaveUtil;
import freemarker.template.TemplateException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.restart.RestartEndpoint;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

import javax.servlet.ServletConfig;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.xml.namespace.QName;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName QueryInfoController
 * @Description: TODO
 * @Author caidao
 * @Date 2020/11/24
 * @Version V1.0
 **/

@Controller
public class QueryInfoController {

    @Autowired
    private FreemarkerUtilNew freemarkerUtilNew;

    @Autowired
    private ElasticsearchUtil elasticsearchUtil;

//    @Autowired
//    private RestartEndpoint restartEndpoint;

    @RequestMapping(value = "/queryInfo")
    public String queryInfo(Map<String, Object> map) throws IOException, TemplateException, ClassNotFoundException {
        map.put("", "");
        String method = "test";
        StringBuffer interfaceMethodStr = new StringBuffer("public String " + method + "(String param) throws Exception;");
        StringBuffer interfaceMethodImplStr =
                new StringBuffer("@WebMethod(\n" +
                        "            operationName=\"" + method + "\", //发布的方法名称\n" +
                        "            exclude=false   // 默认fase: 表示发布该方法  true:表示不发布此方法\n" +
                        "    )\n" +
                        "    @Override\n" +
                        "    public String " + method + "(@WebParam(name=\"param\") String param) {return \"\";}");

        Map temp = new HashMap();
        temp.put("interfaceClassName", "ITest");
        temp.put("interfaceClassNameImpl", "ITestImpl");
        temp.put("interfaceMethod", interfaceMethodStr.toString());
        temp.put("serviceName", "demo");
        temp.put("interfaceMethodImpl", interfaceMethodImplStr.toString());
//        String iSoap = freemarkerUtilNew.freeMarker("ISoap.ftl", temp);
//        System.out.println(iSoap);

        String path = ResourceUtils.getURL("classpath:").getPath();
        // /E:/IDEAWorkspaces/fallbackSystemPlatform/system-platform/target/classes/

        System.out.println("path:" + path);
        path = path.substring(1, path.length());
        System.out.println("path:" + path);

        saveFileByFreemarker(path, "ISoap.ftl", "ITest.java", temp);
        compiler(path + "com/soft/customInterface/" + "ITest.java");

        saveFileByFreemarker(path, "ISoapImpl.ftl", "ITestImpl.java", temp);
        compiler(path + "com/soft/customInterface/" + "ITestImpl.java");

        saveFileByFreemarker(path, "EsbWebServiceConfig.ftl", "EsbWebServiceConfig.java", temp);
        compiler(path + "com/soft/customInterface/" + "EsbWebServiceConfig.java");
//        MyClassLoader2 loader = new MyClassLoader2();
//        Class<?> aClass = loader.findClass(path+"com.soft.customInterface.ITest");
//        loader.findClass(path+"com.soft.customInterface.ITestImpl");
//        loader.findClass(path+"com.soft.customInterface.EsbWebServiceConfig");
//        restartEndpoint.restart();
//        Class.forName( "com.soft.customInterface.ITest");
//        Class.forName( "com.soft.customInterface.ITestImpl");
//        Class.forName( "com.soft.customInterface.EsbWebServiceConfig");


//        //此处http://charles.com/为命名空间，默认是包名的倒序。IQueryUserService=服务接口名+service
//        String endPointAddress = "http://172.77.72.167/:8080/queryUser"; //服务实际地址
//        javax.xml.ws.Service service = javax.xml.ws.Service.create(new QName("http://charles.com/","IQueryUserService"));
//        //IQueryUserPort=服务接口名+Port
//        service.addPort(new QName("http://charles.com/","8080"),
//                javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING, endPointAddress);

//        findClassLoader("", "com.soft.customInterface.ITest");
//        findClassLoader("", "com.soft.customInterface.ITestImpl");
//        findClassLoader("", "com.soft.customInterface.EsbWebServiceConfig");
        return "";
    }


    public boolean saveFileByFreemarker(String path, String freemakerName, String fileName, Map temp) throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
        cfg.setDirectoryForTemplateLoading(new File(path + "templates/"));
        cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_28));
        Template temp1 = cfg.getTemplate(freemakerName);
//        String fileName = "ITest.java";
        File file = new File(path + "com/soft/customInterface/" + fileName);
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        temp1.process(temp, bw);
        bw.flush();
        fw.close();
        return true;
    }

    public void compiler(String fileName) {
        /**
         * ToolProvider类：该类是为查找工具提供者提供方法，例如，编译器的提供者。）
         * getSystemJavaCompiler：获取此平台提供的 Java™ 编程语言编译器。
         */
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        /**
         * getStandardFileManager： 为此工具获取一个标准文件管理器实现的新实例。
         * 参数：
         *    diagnosticListener - 用于非致命诊断信息的诊断侦听器；如果为 null，则使用编译器的默认方法来报告诊断信息
         *    locale - 格式化诊断信息时要应用的语言环境；如果为 null，则使用默认语言环境。
         *    charset - 用于解码字节的字符集；如果为 null，则使用平台默认的字符集
         * 返回：
         *    标准文件管理器
         *
         */
        StandardJavaFileManager fileMgr = compiler.getStandardFileManager(null, null, null);

        /**
         * getJavaFileObjects：获取表示给定文件的文件对象。
         * 参数：
         *    files - 文件数组
         * 返回：
         *    文件对象列表
         */
        Iterable units = fileMgr.getJavaFileObjects(fileName);

        /**
         * getTask：使用给定组件和参数创建编译任务的 future
         * 参数：
         *    out - 用于来自编译器的其他输出的 Writer；如果为 null，则使用 System.err
         *    fileManager - 文件管理器；如果为 null，则使用编译器的标准文件管理器
         *    diagnosticListener - 诊断侦听器；如果为 null，则使用编译器的默认方法报告诊断信息
         *    options - 编译器选项；null 表示没有选项
         *    classes - 类名称（用于注释处理），null 表示没有类名称
         *    compilationUnits - 要编译的编译单元；null 表示没有编译单元
         * 返回：
         *    表示编译的对象
         */
        JavaCompiler.CompilationTask t = compiler.getTask(null, null, null, null, null, units);//编译任务

        //  开始编译
        t.call();

        //  关闭“java编译器”
        try {
            fileMgr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
