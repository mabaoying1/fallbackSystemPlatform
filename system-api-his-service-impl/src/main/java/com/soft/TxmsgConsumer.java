package com.soft;

/**
 * @ClassName TxmsgConsumer
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/13
 * @Version V1.0
 **/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;


//@Component
//@RocketMQMessageListener(topic = "topic_txmsg",consumerGroup = "consumer_txmsg_group_his")
//@Slf4j
public class TxmsgConsumer {
//implements RocketMQListener<String>
//    @Override
//    public void onMessage(String s) {
//        log.info("开始消费消息:{}",s);
//        //解析消息为对象
//        final JSONObject jsonObject = JSON.parseObject(s);
//        MessageEvent accountChangeEvent = JSONObject.parseObject(jsonObject.getString("messageEvent"),MessageEvent.class);
//        //执行
//        //accountInfoService.addAccountInfoBalance(accountChangeEvent);
//    }
}
