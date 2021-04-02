package com.soft.api.service.his.shengguke;

import ctd.net.rpc.util.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 门诊输血计费
 * 医生开输血申请 -> 交叉配血 -> 发血 -> 反写his费用 -> 病人缴费 -> 病人取血 <br>
 *
 * @ClassName TransfusionOVPayBillImpl
 * @Description: TODO
 * @Author caidao
 * @Date 2021/1/15
 * @Version V1.0
 **/
public class TransfusionOVPayBillImpl {
    /**
     *  insert into ms_yj01 (
     *     YJXH , JGID , TJHM , FPHM , MZXH , BRID,
     *     BRXM , KDRQ , KSDM , YSDM , ZXKS , ZXYS,
     *     ZXPB , ZFPB , CFBZ , JZXH , DJZT , SQID,
     *     SQDMC ,TJFL, DJLY
     *  )
     *  values(
     *      #{yjxh} , #{hospitalId} ,#{TJHM,},#{},#{}, #{outpatient_id 门诊号},
     *      #{} , #{order_time} , #{order_dept} , #{order_operperson},#{exc_dept},#{exc_operperson},
     *      0 , 0 ,  ,  , 0, #{apply_no},
     *      #{order_itemtext}, ,
     *  )
     *
     *      insert into ms_yj02 (
     * 			SBXH, YJXH, YLXH, XMLX, YJZX,
     * 			YLDJ,YLSL,HJJE,
     * 			FYGB, ZFBL ,JGID,ybbxje
     * 		)values
     *            ( #{sbxh}, #{yjxh}, 4347, 0, 1,
     * 				#{order_price},#{order_itemcount}, #{order_price} * #{order_itemcount},
     * 				11, 1, 1,0 )
     *
     *
     */

    @Autowired
    private QueryHISKeyCommon queryHISKeyCommon;

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity transfusionOVPayBillImpl(Map<String,Object> map) throws Exception {
        // 获取msyj01医技序号
        long yjxh = queryHISKeyCommon.getMaxANDValide("MS_YJ01",1);
        // 获取msyj02识别序号
        long sbxh = queryHISKeyCommon.getMaxANDValide("MS_YJ02" ,1);

        return null;
    }








}
