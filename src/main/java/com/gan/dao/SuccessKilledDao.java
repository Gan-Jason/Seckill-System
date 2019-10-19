package com.gan.dao;

import com.gan.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName SuccessKilledDao
 * @Author Jason
 * @Description //TODO com.gan.dao
 * @Date 21:15 2019/10/15
 * @Version 1.0
 */
public interface SuccessKilledDao {

    /**
     * @Author Jason
     * @Description //TODO 插入购买明细，可过滤重复
     * @Date 21:17 2019/10/15
     * @Param [seckilled, userPhone]
     * @return int
     */
    int insertSuccessKilled(@Param("seckilled") long seckilled,@Param("userPhone") long userPhone);
    
    /**
     * @Author Jason
     * @Description //TODO 根据秒杀商品的id查询明细Successkilled对象（该对象携带了Seckill秒杀产品对象）
     * @Date 21:19 2019/10/15
     * @Param [seckillId, userPhone]
     * @return com.gan.entity.SuccessKilled
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);

}
