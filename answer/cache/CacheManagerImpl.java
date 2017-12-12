package test.interview.cache;

import android.os.SystemClock;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by wanghaitao on 2017/12/12.
 */

public class CacheManagerImpl<T> implements CacheManager<T> {

    private static final int DEFAULT_SIZE = 1024;
    private static final long DEFAULT_LIFE_TIME = 24*3600*1000;

    private int capacity = DEFAULT_SIZE;
    private long life = DEFAULT_LIFE_TIME;

    private final Map<String,CacheItem<T>> caches = new HashMap<>();

    private final Map<String,CacheLife> cacheLifeMap = new HashMap<>();

    private final Timer timer = new Timer();

    @Override
    public void addCache(String key, T t) {
        ensureCapacity(1);
        if(key == null || t == null){
            throw new IllegalArgumentException("cache key and data can't be null");
        }
        CacheItem<T> cacheItem = createCacheItem(t);
        caches.put(key,cacheItem);

        CacheLife cacheLife = new CacheLife(key,cacheItem);
        cacheLifeMap.put(key,cacheLife);
        timer.schedule(cacheLife,life);
    }

    private CacheItem<T> createCacheItem(T t){
        CacheItem<T> cacheItem = new CacheItem<>();
        cacheItem.data = t;
        cacheItem.useCount = 0;
        cacheItem.cacheTime = SystemClock.elapsedRealtime();
        return cacheItem;
    }

    @Override
    public T getCache(String key) {
        CacheItem<T> cacheItem = caches.get(key);
        cacheItem.useCount++;
        return cacheItem.data;
    }

    @Override
    public void deleteCache(String key) {
        caches.remove(key);
        CacheLife cacheLife = cacheLifeMap.get(key);
        if(cacheLife != null){
            cacheLife.cancel();
        }
    }

    @Override
    public void clearCache() {
        caches.clear();
    }

    @Override
    public void setCapacity(int size) {
        int diff = size - capacity;
        if(diff < 0){//缩小
            deleteLeastUseCache(diff);
        }
        capacity = size;
    }

    @Override
    public void setLife(long life) {
        this.life = life;
    }

    private void ensureCapacity(int addSize){
        if(addSize > capacity){
            throw new OutOfMemoryError("CacheManager out of memory :" + addSize);
        }
        int size = caches.size() + addSize;

        if(size >= capacity){
            deleteLeastUseCache(size - capacity);
        }
    }

    private void deleteLeastUseCache(int deleteCount){
        ArrayList<Map.Entry<String,CacheItem<T>>> list
                = new ArrayList(caches.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, CacheItem<T>>>() {
            @Override
            public int compare(Map.Entry<String, CacheItem<T>> o1, Map.Entry<String, CacheItem<T>> o2) {
                if(o1.getValue().useCount <= o2.getValue().useCount){
                    return -1;
                }else {
                    return 1;
                }
            }
        });

        for(int i = 0;i < deleteCount;i++){
            caches.remove(list.get(i).getKey());
        }

    }

    private class CacheLife extends TimerTask {

        private String key;
        private WeakReference<CacheItem<T>> cacheItem;

        public CacheLife(String key,CacheItem<T> cacheItem){
            this.key = key;
            this.cacheItem = new WeakReference<CacheItem<T>>(cacheItem);
        }

        @Override
        public void run() {
            CacheItem<T> cache = cacheItem.get();
            if(cache == null){
                return;
            }
            long currentTime = SystemClock.elapsedRealtime();
            if(currentTime -  cache.cacheTime > life){
                deleteCache(key);
            }
        }
    }
}
