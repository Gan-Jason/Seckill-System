package com.gan.service;


import com.gan.dto.Exposer;
import com.gan.dto.SeckillExecution;
import com.gan.entity.Seckill;
import com.gan.exception.RepeatKillException;
import com.gan.exception.SeckillCloseException;
import com.gan.exception.SeckillException;

import java.util.List;

/**业务接口:站在使用者(程序员)的角度设计接口
 * 三个方面:1.方法定义粒度，方法定义的要非常清楚2.参数，要越简练越好
 * 3.返回类型(return 类型一定要友好/或者return异常，我们允许的异常)
 *
 */
public interface ISeckillService {


    /**
     * @Author Jason
     * @Description //TODO 查询全部的秒杀记录
     * @Date 23:34 2019/10/17
     * @Param []
     * @return java.util.List<com.gan.entity.Seckill>
     */
    List<Seckill> getSeckillList();

    /**
     * @Author Jason
     * @Description //TODO 查询单个秒杀记录
     * @Date 23:35 2019/10/17
     * @Param [seckillId]
     * @return com.gan.entity.Seckill
     */
    Seckill getById(long seckillId);


    /**
     * @Author Jason
     * @Description //TODO 在秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀时间
     * @Date 23:35 2019/10/17
     * @Param [seckillId]
     * @return com.gan.dto.Exposer
     */
    Exposer exportSeckillUrl(long seckillId);
    /**
     * @Author Jason
     * @Description //TODO 执行秒杀操作，有可能失败，有可能成功，所以要抛出自己定义的异常
     * @Date 23:04 2019/10/18
     * @Param [seckillId, userPhone, md5]
     * @return com.gan.exception.SeckillException
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException,
            RepeatKillException, SeckillCloseException;
}
