package com.soft.api.feign;

import com.codingapi.txlcn.tc.support.DTXUserControls;
import com.codingapi.txlcn.tracing.TracingContext;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.stereotype.Component;

/**
 * @ClassName ILISServiceAPIFallback
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/16
 * @Version V1.0
 **/
@Component
public class ILISServiceAPIFallback implements LISServiceFeign{
    @Override
    public ResponseEntity testTxlcn(String param) {
        DTXUserControls.rollbackGroup(TracingContext.tracing().groupId());
        return ResultMessage.error(400,"服务超时");
    }

    @Override
    public String querylis(String param) {
        return null;
    }

    @Override
    public ResponseEntity emrDocumentRegistryLIS(String paramJson) {
        DTXUserControls.rollbackGroup(TracingContext.tracing().groupId());
        return ResultMessage.error(400,"服务超时");
    }
}
