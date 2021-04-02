package com.soft.api.service.lis.shengguke;

import com.codingapi.txlcn.tc.annotation.DTXPropagation;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.codingapi.txlcn.tc.core.propagation.DTXPropagationResolver;
import com.soft.api.mapper.shengguke.IQueryLisKeyCommonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName QueryLISKeyCommon
 * @Description: TODO
 * @Author caidao
 * @Date 2020/11/24
 * @Version V1.0
 **/
@Service
public class QueryLISKeyCommon {
    private final Logger logger = LoggerFactory.getLogger(QueryLISKeyCommon.class);

    @Autowired
    private IQueryLisKeyCommonMapper queryLisKeyCommonMapper;

    /**
     * 获取DQZ
     * @param bmc
     * @param count
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public long getMaxANDValide(String bmc , int count){
        try {
            long maxLong = getMax(bmc ,count);
            return maxLong;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("QueryLISKeyCommon: "+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("QueryLISKeyCommon: "+e.getMessage());
        }
        return 0;
    }
    /**
     * 传入需要获取的表名及需要的主键数量，如果更新时发现主键值发生变化，则继续获取组件，直到更新不再变化
     * @throws Exception
     * @throws SQLException
     * */
    private long getMax(String bmc, int count) throws SQLException, Exception{
        boolean upateOk = true;
        if(count <= 0) return 0;
//        bmc = bmc.toUpperCase();
        String sysName = "GY_XHB";
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("sysName", sysName);
        map.put("bmc",bmc);
        //获取当前的主键
        String mzxh = queryLisKeyCommonMapper.getXH(map);
        Long max = 0l;
        if(mzxh != null && mzxh != "") {
            max = Long.parseLong(mzxh.toString());
        }
        long maxReturn = 0;
        int ok = 0;
        try {
            map.put("dqz",max + count);
            map.put("olddqz", max);
            int r = queryLisKeyCommonMapper.updateXH(map);
            ok = r;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("QueryLISKeyCommon: "+e.getMessage());
            ok = 0;
        }
        if( ok == 1) {
            maxReturn = max + 1;
        }else{
            upateOk = false;
        }
        while (!upateOk) {
            mzxh = queryLisKeyCommonMapper.getXH(map);
            max = Long.parseLong(mzxh.toString());
            if(max == null)max = 0l;
            try {
                map.put("dqz",max + count);
                map.put("olddqz", max);
                int r = queryLisKeyCommonMapper.updateXH(map);
                ok = r;
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("QueryLISKeyCommon: "+e.getMessage());
                ok = 0;
            }
            if( ok == 1){
                maxReturn = max + 1;
                logger.info("重新获取序号【"+maxReturn+"】");
                break;
            }
        }
        return maxReturn;
    }


}
