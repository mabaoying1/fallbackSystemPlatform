package com.soft.api.service.his.shengguke;

import com.soft.api.mapper.his.shengguke.IExmReportAuditORCanceMapper;
import ctd.net.rpc.util.ResponseEntity;
import ctd.net.rpc.util.ResultMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 检查报告审核、取消审核
 * @ClassName ExmReportAuditORCancel
 * @Description: TODO
 * @Author caidao
 * @Date 2020/11/27
 * @Version V1.0
 **/
@Service
public class ExmReportAuditORCancelImpl {

    private final Logger logger = LoggerFactory.getLogger(ExmReportAuditORCancelImpl.class);

    @Autowired
    private IExmReportAuditORCanceMapper exmReportAuditORCanceMapper;


    /**
     * 检查报告审核通知
     * @param paramMap
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity exmReportAudit(Map<String,Object> paramMap){
        try {
            String  auditDateTime = strToDateFormat(this.getAuditDateTime(paramMap), "T");
            paramMap.put("AUDITDATETIME", auditDateTime);
            paramMap.put("AUDITDOCTOR", this.getAuditDoctor(paramMap));
            int rel = exmReportAuditORCanceMapper.isReportReview(paramMap);
            if(rel < 1){
                return ResultMessage.error(500,"无法进行报告审核,【EMR_JCBG】不存在;【"+paramMap+"】");
            }
            int updateRows = exmReportAuditORCanceMapper.updateJcztStatusAudit(paramMap);
            if(updateRows < 1){
                return ResultMessage.error(500,"检查报告审核通知【"+paramMap+"】上传失败,数据【0】条");
            }
            return ResultMessage.error(200,"检查报告审核通知成功");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("exmReportAuditCancel,参数：【"+paramMap+"】,异常消息："+e.getMessage());
            return ResultMessage.error(500,e.getMessage());
        }
    }


    /**
     * 取消检查报告审核通知
     * @param paramMap
     * @return
     */
    @Transactional(rollbackFor=Exception.class)
    public ResponseEntity exmReportAuditCancel(Map<String,Object> paramMap){
        try {
            int rel = exmReportAuditORCanceMapper.isReportReview(paramMap);
            if(rel < 1){
                return ResultMessage.error(500,"无法进行报告取消审核,【EMR_JCBG】不存在;【"+paramMap+"】");
            }
//            exmReportAuditORCanceMapper.deleteJCBGByID(paramMap);
            int updateRows = exmReportAuditORCanceMapper.updateJcztStatusCancel(paramMap);
            if(updateRows < 1){
                return ResultMessage.error(500,"取消检查报告审核通知，未找到该报告【"+paramMap+"】失败,数据【0】条");
            }
            return ResultMessage.error(200,"取消检查报告审核通知成功");
        }catch (Exception e){
            e.printStackTrace();
            logger.error("exmReportAuditCancel,参数：【"+paramMap+"】,异常消息："+e.getMessage());
            return ResultMessage.error(500,e.getMessage());
        }
    }

    /**
     * 描述：取Item节点下  AuditDateTime 的值
     * @param map
     * @return
     * @return String
     */
    public String getAuditDateTime(Map map){
        String auditDateTime = null;
        Map exmRequest = (Map) map.get("ExmRequest");
        if(exmRequest.get("Item") instanceof List){
            List<Map> listMap = (ArrayList<Map>) exmRequest.get("Item");
            auditDateTime = (String) listMap.get(0).get("AuditDateTime");
        }else{
            Map mapItem = (Map) exmRequest.get("Item");
            auditDateTime = (String) mapItem.get("AuditDateTime");
        }
        return auditDateTime;
    }

    /**
     * 描述：将字符串格式yyyyMMTddHHmmss的字符串转为日期，格式"yyyy-MM-dd HH:mm:ss"
     * @param date 日期格式字符串
     * @param delPara 要删除的字符串，如：yyyyMMTddHHmmss 中多余的【T】，如果没有则传null
     * @throws Exception
     * @author
     * @return String  返回 格式"yyyy-MM-dd HH:mm:ss" 的字符串日期格式
     */
    public static String strToDateFormat(String date, String delPara) throws Exception {
        StringBuffer date1 = new StringBuffer(date);
        if(!StringUtils.isEmpty(delPara)){
            int index = date1.indexOf(delPara);
            if(index != -1){
                date1.deleteCharAt(index);
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.setLenient(false);
        Date newDate = formatter.parse(date1.toString());
        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(newDate);
    }

    /**
     * 描述：取Item节点下  AuditDoctor 的值
     * @param map
     * @return
     * @return String
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public String getAuditDoctor (Map map){
        String auditDoctor  = null;
        Map exmRequest = (Map) map.get("ExmRequest");
        if(exmRequest.get("Item") instanceof List){
            List<Map> listMap = (ArrayList<Map>) exmRequest.get("Item");
            if(listMap.get(0).get("AuditDoctor") instanceof Map){
                Map exeDoctorMap = (Map) listMap.get(0).get("AuditDoctor");
                auditDoctor = (String) exeDoctorMap.get("#text");
            }else{
                auditDoctor = listMap.get(0).get("AuditDoctor").toString();
            }
        }else{
            Map mapItem = (Map) exmRequest.get("Item");
            if(mapItem.get("AuditDoctor") instanceof Map){
                Map exeDoctorMap = (Map) mapItem.get("AuditDoctor");
                auditDoctor = (String) exeDoctorMap.get("#text");
            }else{
                auditDoctor = mapItem.get("AuditDoctor").toString();
            }
        }
        return auditDoctor;
    }

}
