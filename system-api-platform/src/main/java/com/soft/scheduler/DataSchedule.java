package com.soft.scheduler;

import com.soft.controller.JbzyyDiAnController;
import com.soft.controller.JbzyyDiAnMicrobialController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时任务类
 * 迪安外检报告回写定时任务<br>
 *
 * @className: DataSchedule
 * @package: com.soft.scheduler
 * @author: Ljx
 * @date: 2021/03/23 10:55
 */
@Component
public class DataSchedule {

    @Autowired
    private JbzyyDiAnController jbzyyDiAnController;
    @Autowired
    private JbzyyDiAnMicrobialController jbzyyDiAnMicrobialController;

    private static String url;
    private static String clientid;
    private static String clientguid;
    private static String method1;

    @Value("${dian.url}")
    public void setUrl(String url) {
        DataSchedule.url = url;
    }
    @Value("${dian.clientid}")
    public void setClientid(String clientid) {
        DataSchedule.clientid = clientid;
    }
    @Value("${dian.clientguid}")
    public void setClientguid(String clientguid) {
        DataSchedule.clientguid = clientguid;
    }
    @Value("${dian.method1}")
    public void setMethod(String method1) {
        DataSchedule.method1 = method1;
    }
    @Scheduled(cron ="${diantime.corn1}")
    public void starts(){
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        System.out.println("定时任务测试"+ft.format(dNow));

        jbzyyDiAnController.DiAnInspection(url,clientid,clientguid,method1);
    }
}
