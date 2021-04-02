package com.soft.api.feign;

import com.codingapi.txlcn.tc.support.DTXUserControls;
import com.codingapi.txlcn.tracing.TracingContext;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * @ClassName ILisOperateRelateFallback
 * @Description: TODO
 * @Author caidao
 * @Date 2020/12/11
 * @Version V1.0
 **/
@Component
public class ILisOperateRelateAPIFallback  implements HISServiceFeign{
    @Override
    public ResponseEntity queryHISSEQ(String paramJson) {
        DTXUserControls.rollbackGroup(TracingContext.tracing().groupId());
        return ResultMessage.error(400,"服务超时");
    }

    @Override
    public ResponseEntity barcodeCancelYJ01(String paramJson) {
        DTXUserControls.rollbackGroup(TracingContext.tracing().groupId());
        return ResultMessage.error(400,"服务超时");
    }

    @Override
    public ResponseEntity queryCYPB(String paramJson) {
        DTXUserControls.rollbackGroup(TracingContext.tracing().groupId());
        return ResultMessage.error(400,"服务超时");
    }

    @Override
    public ResponseEntity cancelLISPay(String paramJson) throws Exception {
        DTXUserControls.rollbackGroup(TracingContext.tracing().groupId());
        return ResultMessage.error(400,"服务超时");
    }

    @Override
    public ResponseEntity lisPay(String paramJson) throws Exception {
        DTXUserControls.rollbackGroup(TracingContext.tracing().groupId());
        return ResultMessage.error(400,"服务超时");
    }

    @Override
    public ResponseEntity updateMzZxbz(String paramJson) throws Exception {
        DTXUserControls.rollbackGroup(TracingContext.tracing().groupId());
        return ResultMessage.error(400,"服务超时");
    }

    @Override
    public ResponseEntity queryYZXHStr(String paramJson) {
        DTXUserControls.rollbackGroup(TracingContext.tracing().groupId());
        return ResultMessage.error(400,"服务超时");
    }
}
