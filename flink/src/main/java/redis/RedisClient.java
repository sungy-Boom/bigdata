package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author sunguiyong
 * @date 2024/5/20 17:49
 */
public class RedisClient {
    private volatile static Jedis jedisClient;
    private static JedisPool jedisPool = new JedisPool();

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(20);
        config.setMaxIdle(10);
        config.setMinIdle(10);

        jedisPool = new JedisPool(config, "127.0.0.1", 6379);
    }

    static Jedis initRedisClient() {
        if (jedisPool != null) {
            return jedisPool.getResource();
        }
        synchronized (RedisClient.class) {
            if (jedisPool != null) {
                return jedisPool.getResource();
            }
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(50);
            config.setMaxIdle(10);
            config.setMinIdle(10);
            jedisPool = new JedisPool(config, "127.0.0.1", 6379, 3000);
            return jedisPool.getResource();
        }
    }

    public void set(String key, String value) {
        try (Jedis jedis = RedisClient.initRedisClient()) {
            jedis.set(key, value);
        } catch (Exception e) {
            System.out.println("set error " + e.getMessage());
        }
    }

    public void hset(String key, String field, String value) {
        try (Jedis jedis = RedisClient.initRedisClient()) {
            jedis.hset(key, field, value);
        } catch (Exception e) {
            System.out.println("set error " + e.getMessage());
        }
    }
}
