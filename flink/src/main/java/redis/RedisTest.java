package redis;

import redis.clients.jedis.Jedis;

/**
 * @author sunguiyong
 * @date 2022/1/19 4:20 下午
 */
public class RedisTest {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("172.30.248.112", 6379);
        jedis.set("test", "test");
        String test = jedis.get("test");
        System.out.println();
    }
}
