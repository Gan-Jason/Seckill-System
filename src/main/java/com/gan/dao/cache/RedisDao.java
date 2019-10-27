package com.gan.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.gan.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;



public class RedisDao {
    Logger logger = LoggerFactory.getLogger(RedisDao.class);
    //redis连接池
    private final JedisPool jedisPool;
    //反序列化用的对象模板
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);


    //获取redis连接
    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }

    public Seckill getSeckill(long seckillId) {
        return getSeckill(seckillId, null);
    }

    /**
     * @return com.gan.entity.Seckill   如果不存在，则返回null
     * @Author Jason
     * @Description //TODO 从redis获取信息
     * @Date 11:14 2019/10/26
     * @Param [seckillId, jedis]
     */
    private Seckill getSeckill(long seckillId, Jedis jedis) {
        boolean hasJedis = jedis != null;
        try {
            if (!hasJedis) {
                jedis = jedisPool.getResource();
            }
            try {
                //根据对象id获取redis中的对象的key
                String key = getSeckillRedisKey(seckillId);
                //采用自定义序列化
                //先从redis拿到二进制数据
                byte[] bytes = jedis.get(key.getBytes());
                logger.info("Get bytes:",bytes);
                if (bytes != null) {
                    //redis中信息不为空，则先new一个空对象，作为壳
                    Seckill seckill = schema.newMessage();
                    //再通过该对象的模板，把数据反序列化到壳中
                    ProtobufIOUtil.mergeFrom(bytes, seckill, schema);
                    return seckill;

                }
            } finally {
                if (!hasJedis) {
                    jedis.close();
                }

            }


        } catch (Exception e) {
            logger.error("error:",e);

        }
        return null;
    }

    private String getSeckillRedisKey(long seckillId) {
        return "seckill:" + seckillId;
    }

    public String putSeckill(Seckill seckill){
        return putSeckill(seckill,null);
    }
    private String putSeckill(Seckill seckill,Jedis jedis){
        boolean hasJedis=jedis!=null;
        try{
            if(!hasJedis){
                jedis=jedisPool.getResource();
            }
            try{
                String key=getSeckillRedisKey(seckill.getSeckillId());
                byte[] bytes=ProtobufIOUtil.toByteArray(seckill,schema,
                                //这是一个缓存器，大小为默认大小
                                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                logger.info("Put bytes:",bytes);
                int timeout=60*60;
                String result=jedis.setex(key.getBytes(),timeout,bytes);
                return result;
            }finally {
                if(!hasJedis){
                    jedis.close();
                }
            }
        }catch (Exception e){
            logger.error("error:",e);
        }
        return null;
    }
}
