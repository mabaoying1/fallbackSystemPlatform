package com.soft.api.service.lis.shengguke;
import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.soft.api.mapper.shengguke.EMRDocumentRegistryMapper;
import com.soft.api.service.ILISQueryService;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

/**
 * 实现LIS接口 <br>
 * @ClassName LISShengGuKeImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2020/11/24
 * @Version V1.0
 **/
@RestController
public class LISShengGuKeImpl implements ILISQueryService {

    @Autowired
    private EMRDocumentRegistryLISImpl emrDocumentRegistryLISSGKImpl;

    @Autowired
    private EMRDocumentRegistryMapper emrDocumentRegistryMapper;

    @RequestMapping(value="/testTxlcn")
    @Override
    @LcnTransaction(propagation = DTXPropagation.SUPPORTS)
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity testTxlcn(String param) {
        Map map = new HashMap();
        map.put("id","1");
        map.put("name","hello");
        emrDocumentRegistryMapper.testLcn(map);
        return ResultMessage.success(200);
    }

    @RequestMapping(value="/querylis")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String querylis(String param) {
        System.out.println("param："+param);
        return "success";
    }

    @Override
    @PostMapping(value = "/emrDocumentRegistryLIS")
    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity emrDocumentRegistryLIS(String paramJson) throws Exception {
        return emrDocumentRegistryLISSGKImpl.emrDocumentRegistry(paramJson);
    }

}
