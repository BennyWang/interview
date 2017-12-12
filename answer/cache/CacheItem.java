package test.interview.cache;

/**
 * Created by wanghaitao on 2017/12/12.
 */

class CacheItem<T> {
    public T data;
    protected long cacheTime;
    protected long useCount;
}
