package com.soft.api.feign;

import com.soft.api.service.shengguke.ILisOperateRelate;
import org.springframework.cloud.openfeign.FeignClient;


@FeignClient(value="app-soft-his",fallback = ILisOperateRelateAPIFallback.class) //
public interface HISServiceFeign extends ILisOperateRelate {
}
