package com.tsien.mall.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.tsien.mall.constant.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/6/27 0027 22:11
 */

public class TokenCacheUtil {

    private static Logger logger = LoggerFactory.getLogger(TokenCacheUtil.class);

    public static String TOKEN_PREFIX = "token_";

    /**
     * 声明一个静态的内存块,guava里面的本地缓存
     * 构建本地缓存，调用链的方式 ,1000是设置缓存的初始化容量，maximumSize是设置缓存最大容量，当超过了最大容量，guava将使用LRU算法（最少使用算法），来移除缓存项
     * expireAfterAccess(12,TimeUnit.HOURS)设置缓存有效期为12个小时
     */
    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder()
            .initialCapacity(1000).maximumSize(10000)
            .expireAfterAccess(12, TimeUnit.HOURS)
            .build(
                    //这个方法是默认的数据加载实现,get的时候，如果key没有对应的值，就调用这个方法进行加载
                    new CacheLoader<>() {
                        // 默认数据加载实现，当调用get取值的时候，如果key没有对用的值，就用这个方法进行加载。
                        @Override
                        public String load(String s) throws Exception {
                            // 为什么要把return的null值写成字符串，因为到时候用null去.equal的时候，会报空指针异常
                            return "null";
                        }
                    });

    /**
     * 添加本地缓存
     *
     * @param key   key
     * @param value value
     */
    public static void setKey(String key, String value) {
        localCache.put(key, value);
    }

    /**
     * 获取本地缓存
     *
     * @param key key
     * @return 本地缓存
     */
    public static String getkey(String key) {
        String value = null;
        try {
            value = localCache.get(key);
            if (Const.NULL.equals(value)) {
                return null;
            }
            return value;
        } catch (Exception e) {
            logger.error("localCache get error", e);
        }
        return null;
    }

}
