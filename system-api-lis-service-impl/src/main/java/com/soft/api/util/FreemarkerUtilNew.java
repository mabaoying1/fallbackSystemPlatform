package com.soft.api.util;

import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

/**
 * @ClassName FreemarkerUtilNew
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/9
 * @Version V1.0
 **/
@Slf4j
@Component
public class FreemarkerUtilNew {


    @Autowired
    private FreeMarkerConfigurer configurer;

    public String freeMarker(String xml , Object obj) {
        String resStr = "";
        try {
            Template template = configurer.getConfiguration()
                    .getTemplate(xml);
            resStr = FreeMarkerTemplateUtils.processTemplateIntoString(template,
                    obj);
        }catch (Exception e){
            log.error("生成报表freemark：{}；参数：{},"+"【"+xml+"】;"+ obj);
            e.printStackTrace();
        }
        return resStr;
    }

}
