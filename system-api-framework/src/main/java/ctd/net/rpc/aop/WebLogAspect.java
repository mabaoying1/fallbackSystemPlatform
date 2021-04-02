package ctd.net.rpc.aop;

import ctd.net.rpc.Entity.SysParameterEntity;
import ctd.net.rpc.util.DateTimeFormatterUtil;
import ctd.net.rpc.util.ElasticsearchUtil;
import ctd.net.rpc.util.ObjectToMap;
import ctd.net.rpc.util.UUIDUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WebLogAspect
 * @Description: TODO
 * @Author caidao
 * @Date 2021/2/23
 * @Version V1.0
 **/
@Aspect
@Component
public class WebLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    @Autowired
    private ElasticsearchUtil elasticsearchUtil;

    @Autowired
    private SysParameterEntity sysParameterEntity;

    @Pointcut("execution(* com.soft.ws..*(..))")
    public void rlAop() {
    }

    //该方法是一个前置通知，在目标方法开始之前执行
    @Before("rlAop()")
    public void beforeMethod(JoinPoint joinPoint) {
         /**  自己定义注解时使用
         MethodSignature signature = (MethodSignature) point.getSignature();
         ExtApiToken extApiToken = signature.getMethod().getDeclaredAnnotation(ExtApiToken.class); //获取当前方法是否使用ExtApiToken注解
         if (extApiToken != null) {
         // 可以放入到AOP代码 前置通知  使用注解，执行下列方法
         }
         */
//        //获取当前执行的方法名
//        String methodName = joinPoint.getSignature().getName();
//        //获取当前运行的对象
//        List<Object> args = Arrays.asList(joinPoint.getArgs());
//        System.out.println("前置通知：The method" + methodName + " begins with " + args);
    }


    //后置通知：在目标方法执行后（无论是否发生异常），执行的通知
    //在后置通知中还不能访问目标方法执行的结果
    @After("rlAop()")
    public void afterMethod(JoinPoint joinPoint) {
        //获取当前执行的方法名
//        String methodName = joinPoint.getSignature().getName();
//        System.out.println("后置通知：The method " + methodName + " ends");
    }

    //返回通知:在方法正常结束后反回的通知,可以访问到方法的返回值
    //没怎么注意项目中使用
    @AfterReturning(value = "rlAop()",returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
//        String methodName = joinPoint.getSignature().getName();
//        System.out.println("返回通知：The method " + methodName + " ends with " + result);

    }

    /**
     * 在目标方法出现异常时会执行的代码
     * 可以访问到异常对象；且可以指定在出现特定异常时在执行通知代码
     * @param joinPoint
     * @param ex
     */
    //异常通知   一般用于撤销事物，获取记录异常日志value = "rlAop()",
    @AfterThrowing(throwing = "ex", value = "rlAop()")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) {
//        String methodName = joinPoint.getSignature().getName();
//        System.out.println("异常通知：The method " + methodName + " occurs exception: " + ex);
    }


    /**
     * 环绕通知需要携带 ProceedingJoinPoint 类型的参数
     * 环绕通知类似于动态代理的全过程：ProceedingJoinPoint 类型的参数可以决定是否执行目标方法
     * 且环绕通知必须有返回值，返回值必须为目标方法的返回值
     * 用于日志记录和事物管理，可以用于注解开发
     */
    @Around("rlAop()")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) {
        Object result = null;
        String methodName = proceedingJoinPoint.getSignature().getName();
        Map logMap = new HashMap();
        //执行目标方法
        try {
            Map<String, Object> paramMap = null;
            String paramStr = Arrays.toString(proceedingJoinPoint.getArgs())
                    .replace("[","").replace("]","");
            String pType = ObjectToMap.isXmlORJson(paramStr);
            if("XML".equals(pType)){
                paramMap = ObjectToMap.getObjectToMap(paramStr);
            }
            logMap.put("methodName" , methodName);
            logMap.put("InParameter" , paramMap);
            logMap.put("InParameterDateTime" , DateTimeFormatterUtil.format(LocalDateTime.now(),""));
            //前置通知
            //System.out.println("环绕通知(前置)：The method " + methodName + " begins with " + Arrays.asList(proceedingJoinPoint.getArgs()));
            result = proceedingJoinPoint.proceed();//环绕通知获取proceed，执行拦截的方法 获取结果即可放行
            Map<String, Object> reultMap = null;
            String type = ObjectToMap.isXmlORJson(result.toString());
            if("1".equals(sysParameterEntity.getIsElasticsearch())){ //存放记录到es
                if("XML".equals(type) || "JSON".equals(type)){
                    reultMap = ObjectToMap.getObjectToMap(result.toString());
                    logMap.put("OutParameter" , reultMap);
                    logMap.put("OutParameterDateTime" , DateTimeFormatterUtil.format(LocalDateTime.now(),""));
                    String uuid = UUIDUtil.getUUID();
                    if(methodName.toUpperCase().indexOf("QUERY") != -1){ //表示查询接口
                        elasticsearchUtil.addElasticsearchData(logMap ,"query","query", uuid);
                    }else{ //业务操作
                        elasticsearchUtil.addElasticsearchData(logMap ,"business","business", uuid);
                    }
                }
            }
            //后置通知
            //System.out.println("环绕通知(后置)The method ends with "+result);
        } catch (Throwable e) {
            e.printStackTrace();
            //System.out.println("环绕通知(异常)The method ocuurs exception: " + e );
            throw new RuntimeException(e);
        }
        //System.out.println("环绕通知(后置) The method ends");
        return result;
    }
}
