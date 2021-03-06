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
                        "            operationName=\"" + method + "\", //?????????????????????\n" +
                        "            exclude=false   // ??????fase: ?????????????????????  true:????????????????????????\n" +
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


//        //??????http://charles.com/?????????????????????????????????????????????IQueryUserService=???????????????+service
//        String endPointAddress = "http://172.77.72.167/:8080/queryUser"; //??????????????????
//        javax.xml.ws.Service service = javax.xml.ws.Service.create(new QName("http://charles.com/","IQueryUserService"));
//        //IQueryUserPort=???????????????+Port
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
         * ToolProvider??????????????????????????????????????????????????????????????????????????????????????????
         * getSystemJavaCompiler??????????????????????????? Java??? ????????????????????????
         */
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        /**
         * getStandardFileManager??? ??????????????????????????????????????????????????????????????????
         * ?????????
         *    diagnosticListener - ????????????????????????????????????????????????????????? null?????????????????????????????????????????????????????????
         *    locale - ???????????????????????????????????????????????????????????? null?????????????????????????????????
         *    charset - ?????????????????????????????????????????? null????????????????????????????????????
         * ?????????
         *    ?????????????????????
         *
         */
        StandardJavaFileManager fileMgr = compiler.getStandardFileManager(null, null, null);

        /**
         * getJavaFileObjects?????????????????????????????????????????????
         * ?????????
         *    files - ????????????
         * ?????????
         *    ??????????????????
         */
        Iterable units = fileMgr.getJavaFileObjects(fileName);

        /**
         * getTask??????????????????????????????????????????????????? future
         * ?????????
         *    out - ??????????????????????????????????????? Writer???????????? null???????????? System.err
         *    fileManager - ??????????????????????????? null?????????????????????????????????????????????
         *    diagnosticListener - ??????????????????????????? null??????????????????????????????????????????????????????
         *    options - ??????????????????null ??????????????????
         *    classes - ????????????????????????????????????null ?????????????????????
         *    compilationUnits - ???????????????????????????null ????????????????????????
         * ?????????
         *    ?????????????????????
         */
        JavaCompiler.CompilationTask t = compiler.getTask(null, null, null, null, null, units);//????????????

        //  ????????????
        t.call();

        //  ?????????java????????????
        try {
            fileMgr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
