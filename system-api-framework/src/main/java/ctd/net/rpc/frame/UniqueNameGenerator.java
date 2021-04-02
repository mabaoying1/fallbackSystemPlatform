package ctd.net.rpc.frame;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

/**
 * 解决不同文件夹下相同bean注入问题，使用全路径
 * @ClassName UniqueNameGenerator
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/1
 * @Version V1.0
 **/
public class UniqueNameGenerator extends AnnotationBeanNameGenerator {
    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        //全限定类名
        String beanName = definition.getBeanClassName();
        return beanName;
    }
}
