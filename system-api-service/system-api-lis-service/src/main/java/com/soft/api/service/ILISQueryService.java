package com.soft.api.service;

import ctd.net.rpc.util.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface ILISQueryService {

    @RequestMapping(value="/testTxlcn")
    public ResponseEntity testTxlcn(String param);

    @RequestMapping(value="/querylis")
    public String querylis(String param);

    /**
     * 回写检验报告
     * @param paramJson
     * @return
     */
    @PostMapping(value="/emrDocumentRegistryLIS")
    public ResponseEntity emrDocumentRegistryLIS(@RequestParam String paramJson) throws Exception;
}
