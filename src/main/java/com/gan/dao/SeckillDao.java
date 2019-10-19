package com.gan.dao;

import com.gan.entity.Seckill;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


//DAO层工作演变为：接口设计+SQL编写


//@Repository
public interface SeckillDao {
    
    /**
     * 减库存
     * @Author Jason
     * @Description //TODO 如果影响行数>1，表示更新库存的记录行数
     * @Date 21:12 2019/10/15
     * @Param [seckillId, killTime]
     * @return int
     */
    int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);
    
    /**
     * @ClassName 根据id查询秒杀的商品信息
     * @Author Jason
     * @Description //TODO com.gan.dao
     * @Date 21:13 2019/10/15
     * @Version 1.0
     */
    Seckill queryById(long seckillId);
    
    /**
     * @Author Jason
     * @Description //TODO 根据偏移量查询秒杀商品列表
     * @Date 23:02 2019/10/15
     * @Param [offset, limit]
     * @return java.util.List<com.gan.entity.Seckill>
     */
    List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
}
