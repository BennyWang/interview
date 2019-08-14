package test.interview.cache;

/**
 * Created by wanghaitao on 2017/12/12.
 */

public interface CacheManager <T>{

    /**
     * 添加缓存
     * @param key
     * @param t
     */
    void addCache(String key,T t);

    /**
     * 获取缓存
     * @param key
     * @return
     */
    T getCache(String key);

    /**
     * 删除缓存
     * @param key
     */
    void deleteCache(String key);

    /**
     * 清空缓存
     */
    void clearCache();

    /**
     * 设置缓存容量
     * @param size
     */
    void setCapacity(int size);

    /**
     * 设置缓存生命周期
     * @param life
     */
    void setLife(long life);
}
