package com.gan.service.impl;

import com.gan.dto.Exposer;
import com.gan.dto.SeckillExecution;
import com.gan.entity.Seckill;
import com.gan.exception.RepeatKillException;
import com.gan.exception.SeckillCloseException;
import com.gan.service.ISeckillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {


    private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISeckillService seckillService;
    @Test
    public void getSeckillList() {
        List<Seckill> seckills=seckillService.getSeckillList();
        logger.info("seckillLists={}",seckills);
    }

    @Test
    public void getById() {
        Seckill seckill=seckillService.getById(1000);
        logger.info("Entity seckill={}",seckill);
    }

    @Test
    public void exportSeckillUrl() {
        Exposer exposer=seckillService.exportSeckillUrl(1000);
        logger.info("Exposer={}",exposer);

    }

    @Test
    public void executeSeckill() {
        long seckillId=1000;
        Exposer exposer=seckillService.exportSeckillUrl(seckillId);
        if(exposer.isExposed()){
            long userPhone=18817384608L;
            String md5=exposer.getMd5();

            try{
                SeckillExecution seckillExecution=seckillService.executeSeckill(seckillId,userPhone,md5);
                logger.info("seckillExecution={}",seckillExecution);
            }catch (RepeatKillException e){
                logger.warn("error:",e);

            }catch (SeckillCloseException e1){
                logger.warn("error:",e1);
            }
        }else{
            logger.warn("秒杀未开始：{}",exposer);
        }
    }
}