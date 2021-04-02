package com.soft.framework;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName WSAuthInterceptor
 * @Description: TODO
 * @Author caidao
 * @Date 2020/9/29
 * @Version V1.0
 **/
public class WSAuthInterceptor extends AbstractPhaseInterceptor<SoapMessage> {

    private final Logger logger = LoggerFactory.getLogger(WSAuthInterceptor.class);

    public WSAuthInterceptor() {
        super(Phase.PRE_INVOKE);
    }

    @Override
    public void handleMessage(SoapMessage soapMessage) throws Fault {
        logger.info("WSAuthInterceptor.................................");

    }
}
