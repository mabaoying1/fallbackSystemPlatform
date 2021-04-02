package com.soft.api.service.shengguke;

import ctd.net.rpc.util.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * lis 相关
 */
public interface ILisOperateRelate {

    /**
     * 查询HIS序列
     * @param paramJson
     * @return
     */
    @RequestMapping(value="/queryHISSEQ")
    public ResponseEntity queryHISSEQ(@RequestParam String paramJson);

    /**
     * lis取消条码打印通知
     * @param paramJson
     * @return
     */
    @RequestMapping(value="/barcodeCancelYJ01")
    public ResponseEntity barcodeCancelYJ01(@RequestParam String paramJson);

    /**
     * 判断患者是否已经出院
     * @param paramJson
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/queryCYPB")
    public ResponseEntity queryCYPB(@RequestParam String paramJson) throws Exception;

    /**
     * lis取消条码打印取消计费
     * @param paramJson
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/cancelLISPay")
    public ResponseEntity cancelLISPay(@RequestParam String paramJson) throws Exception;

    /**
     * lis 条码打印HIS计费
     * @param paramJson
     * @return
     */
    @PostMapping(value = "/lisPay")
    public ResponseEntity lisPay(@RequestParam String paramJson) throws Exception;

    /**
     * lis条码打印，更新门诊执行标志 <br>
     * 更新MS_YJ01的zxrq、zxys、zxpb
     * @param paramJson
     * @return
     */
    @RequestMapping(value="/updateMzZxbz")
    public ResponseEntity updateMzZxbz(@RequestParam String paramJson) throws Exception;

    /**
     * lis 条码打印，获取YZXH
     * @return
     *
     * @RequestParam String YSYZBH ,
     * @RequestParam String REQUESTID,
     * @RequestParam String YLXH
     */
    @RequestMapping(value = "/queryYZXHStr" , method = RequestMethod.POST)
    public ResponseEntity queryYZXHStr(@RequestParam String paramJson);
}
