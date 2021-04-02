package com.soft.api.service.his.shengguke;

import com.soft.api.mapper.his.shengguke.IQueryHISKeyCommonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询HIS主键（重庆版本HIS主键存放在HIS用户下，未在portal）
 * @ClassName QueryHISKeyCommon
 * @Description: TODO
 * @Author caidao
 * @Date 2020/11/24
 * @Version V1.0
 **/
@Service
public class QueryHISKeyCommon {
    private final Logger logger = LoggerFactory.getLogger(QueryHISKeyCommon.class);

    @Autowired
    private IQueryHISKeyCommonMapper queryHISKeyCommonMapper;

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
            logger.error("QueryHISKeyCommon: "+e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("QueryHISKeyCommon: "+e.getMessage());
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
        bmc = bmc.toUpperCase();
        Integer col;
        String sysName ="GY_IDENTITY";
        String temp = "";
        col = bmc.indexOf("_");
        temp = bmc.substring(0, 2);
        if(col == 0 || "GY".equals(temp) ){
            sysName ="GY_IDENTITY";
        }else{
            sysName ="GY_IDENTITY_" + bmc.substring(0, col).toUpperCase();
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("sysName", sysName);
        map.put("bmc",bmc);
        //获取当前的主键
        String mzxh = queryHISKeyCommonMapper.getXH(map);
        Long max = 0l;
        if(mzxh != null && mzxh != "") {
            max = Long.parseLong(mzxh.toString());
        }
        long maxReturn = 0;
        int ok = 0;
        try {
            map.put("dqz",max + count);
            map.put("olddqz", max);
            int r = queryHISKeyCommonMapper.updateXH(map);
            ok = r;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("QueryHISKeyCommon: "+e.getMessage());
            ok = 0;
        }
        if( ok == 1) {
            maxReturn = max + 1;
        }else{
            upateOk = false;
        }
        while (!upateOk) {
            mzxh = queryHISKeyCommonMapper.getXH(map);
            max = Long.parseLong(mzxh.toString());
            if(max == null)max = 0l;
            try {
                map.put("dqz",max + count);
                map.put("olddqz", max);
                int r = queryHISKeyCommonMapper.updateXH(map);
                ok = r;
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("QueryHISKeyCommon: "+e.getMessage());
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
