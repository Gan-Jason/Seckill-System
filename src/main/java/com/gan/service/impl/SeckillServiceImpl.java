package com.gan.service.impl;

import com.gan.dao.SeckillDao;
import com.gan.dao.SuccessKilledDao;
import com.gan.dto.Exposer;
import com.gan.dto.SeckillExecution;
import com.gan.entity.Seckill;
import com.gan.entity.SuccessKilled;
import com.gan.enums.SeckillStatEnum;
import com.gan.exception.RepeatKillException;
import com.gan.exception.SeckillCloseException;
import com.gan.exception.SeckillException;
import com.gan.service.ISeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

@Service
/**
 * @ClassName SeckillServiceImpl
 * @Author Jason
 * @Description //TODO com.gan.service.impl
 * @Date 23:03 2019/10/18
 * @Version 1.0
 */
public class SeckillServiceImpl implements ISeckillService {

    //日志对象
    private Logger logger= LoggerFactory.getLogger(this.getClass());
    //加入一个混淆字符串(秒杀接口)的salt，为了我避免用户猜出md5值，值任意给，越复杂越好
    private final String salt="safw4t32tgrd2532(&(*@&";
    @Autowired
    private SeckillDao  seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;


    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = getById(seckillId);

        //若秒杀未开启
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();

        //系统当前时间
        Date nowTime = new Date();
        if (startTime.getTime() > nowTime.getTime() || endTime.getTime() < nowTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(),
                    endTime.getTime());

        }

        //秒杀开始，返回秒杀商品的id，用给接口加密的md5，MD5不可逆
        String md5 = getMd5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMd5(long seckillId){


        //秒杀开启，返回秒杀商品的id，用给接口加密的md5
        String base=seckillId+"/"+salt;
        String md5= DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    /**
     * @Author Jason
     * @Description //秒杀是否成功，成功：减库存，增加明细，失败：抛出异常，事务回滚
     * @Date 23:19 2019/10/18
     * @Param [seckillId, userPhone, md5]
     * @return com.gan.dto.SeckillExecution
     *
     *
     */

    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException,
            RepeatKillException, SeckillCloseException {
        if(md5==null||!md5.equals(getMd5(seckillId))){
            throw new SeckillException("Seckill data rewrote");
        }

        Date nowTime=new Date();

        try{

            int insertCount=successKilledDao.insertSuccessKilled(seckillId,userPhone);
            if(insertCount<=0){
                throw new RepeatKillException("Seckill repeated");
            }else{
                int updateCount=seckillDao.reduceNumber(seckillId,nowTime);
                if(updateCount<=0){
                    throw new SeckillCloseException("Seckill is closed");
                }else{
                    SuccessKilled successKilled=successKilledDao.queryByIdWithSeckill(seckillId,userPhone);

                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);

                }
            }
        }catch (SeckillCloseException e1){
            throw e1;

        }catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            throw new SeckillException("Seckill inner error:"+e.getMessage());
        }

    }
}
