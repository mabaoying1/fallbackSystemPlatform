package com.soft.init;

import com.soft.util.CallAxisWebservice;
import com.soft.util.CallHttpPostWebservice;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName LoadORMXml
 * @Description: TODO 加载映射关系
 * @Author caidao
 * @Date 2020/10/20
 * @Version V1.0
 **/
@Component
@Order(value = 2)//执行顺序由小到大
public class LoadORMXml implements CommandLineRunner {

    @Autowired
    private CallAxisWebservice callHttpPostWebservice;

    @Override
    public void run(String... args) throws Exception {
//        MultiValueMap<String, Object> param = new LinkedMultiValueMap<String, Object>();
//        param.set("serviceID","ba");
//        param.set("zyh","");

//        Object[] obj = new Object[]{"ba","123"};
//        Object str = callHttpPostWebservice.invokeRemoteFuc("operationRequest","123,ba");
//        System.out.println(">>>>>>>>>>>"+str);


//        //初始化映射关系
//        //主手术记录
//        ClassPathResource classPathResource = new ClassPathResource("orm/drgsOperationORM.xml");
//        //1.创建Reader对象
//        SAXReader reader = new SAXReader();
//        //2.加载xml
//        Document document = reader.read(classPathResource.getInputStream());
//        //3.获取根节点
//        Element rootElement = document.getRootElement();
//        Iterator iterator = rootElement.elementIterator();
//        while (iterator.hasNext()){
//            Element stu = (Element) iterator.next();
//            List<Attribute> attributes = stu.attributes();
//            System.out.println("======获取属性值======");
//            for (Attribute attribute : attributes) {
//                System.out.println(attribute.getValue());
//            }
//            System.out.println(stu.getText());
//            System.out.println("======遍历子节点======");
//            Iterator iterator1 = stu.elementIterator();
//            while (iterator1.hasNext()){
//                Element stuChild = (Element) iterator1.next();
//                System.out.println("节点名："+stuChild.getName()+"---节点值："+stuChild.getStringValue());
//            }
//        }
    }
}
