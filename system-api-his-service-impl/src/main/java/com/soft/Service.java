package com.soft;

/**
 * @ClassName Service
 * @Description: TODO
 * @Author caidao
 * @Date 2020/10/13
 * @Version V1.0
 **/
public class Service {


//    /**
//     * 消费消息，更新本地事务，添加金额
//     * @param accountChangeEvent
//     */
//    @Override
//    @Transactional
//    public void addAccountInfoBalance(AccountChangeEvent accountChangeEvent) {
//        log.info("bank2更新本地账号，账号：{},金额：{}",accountChangeEvent.getAccountNo(),accountChangeEvent.getAmount());
//
//        //幂等校验
//        int existTx = accountInfoDao.isExistTx(accountChangeEvent.getTxNo());
//        if(existTx<=0){
//            //执行更新
//            accountInfoDao.updateAccountBalance(accountChangeEvent.getAccountNo(),accountChangeEvent.getAmount());
//            //添加事务记录
//            accountInfoDao.addTx(accountChangeEvent.getTxNo());
//            log.info("更新本地事务执行成功，本次事务号: {}", accountChangeEvent.getTxNo());
//        }else{
//            log.info("更新本地事务执行失败，本次事务号: {}", accountChangeEvent.getTxNo());
//        }
//
//    }

}
