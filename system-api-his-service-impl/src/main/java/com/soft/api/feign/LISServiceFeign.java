package com.soft.api.feign;

import com.soft.api.service.ILISQueryService;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @ClassName LISServiceFeign
 * @Description: TODO
 * @Author caidao
 * @Date 2020/9/21
 * @Version V1.0
 **/

@FeignClient(value="app-soft-lis",fallback = ILISServiceAPIFallback.class)
public interface LISServiceFeign extends ILISQueryService {
}
