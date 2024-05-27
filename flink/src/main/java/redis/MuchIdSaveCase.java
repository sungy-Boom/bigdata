package redis;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author sunguiyong
 * @date 2024/5/20 17:41
 */
public class MuchIdSaveCase {

    private final AtomicLong count = new AtomicLong(0);
    private final Map<String, AtomicLong> threadCntMap = new HashMap<>();
    ExecutorService executorService = Executors.newFixedThreadPool(100);
    private static final RedisClient redisClient = new RedisClient();

    public void generateAndSaveId(int flag) throws InterruptedException {
        while (count.get() <= 1000000) {
            executorService.execute(() -> doingGenAndSave(flag));
            count.incrementAndGet();
        }

        executorService.shutdown();
        if (executorService.awaitTermination(300, TimeUnit.SECONDS)) {
            System.out.println(count.get());
        }

        long calCnt = threadCntMap.values().stream().mapToLong(AtomicLong::get).sum();
        System.out.println(calCnt);
    }

    private void doingGenAndSave(int flag) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        try {
            lock.writeLock().lock();
            String key = generateId();
            String value = generateId();
            if (flag == 1) {
                redisClient.set(key, value);
            } else if (flag == 2) {
                //计算hash值，取0～999分桶
                int bucket = Math.abs(key.hashCode() % 5000);
                redisClient.hset(String.valueOf(bucket), key, value);
            }
            String threadName = Thread.currentThread().getName();
            if (threadCntMap.containsKey(threadName)) {
                AtomicLong tmp = threadCntMap.get(threadName);
                tmp.incrementAndGet();
                threadCntMap.put(threadName, tmp);
            } else {
                threadCntMap.put(threadName, new AtomicLong(1));
            }
        } catch (Exception e) {
            System.out.println("lock error " + e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }

    private String generateId() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.substring(0, 10);
        return uuid;
    }

    public static void main(String[] args) throws InterruptedException {
        Integer[] lists = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        PriorityQueue<Integer> pq = new PriorityQueue<>(
                lists.length, Comparator.comparingInt(a -> a));
        MuchIdSaveCase caseInstance = new MuchIdSaveCase();
        caseInstance.generateAndSaveId(2);
    }
}
