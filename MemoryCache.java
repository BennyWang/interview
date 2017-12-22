package Android.Android;

import java.util.LinkedList;
import java.util.ListIterator;

public class MemoryCache<KO, VO> {
	public static final int CAPACITY_DEFAULT = 10; // 默认缓存
	public static final int TIME_DEFAULT = 10;// 默认时长

    private final LinkedList<ValueHolder<KO, VO>> mCacheList; // 缓存
    private final LinkedList<Integer> mcishuList; // 次数
    private final LinkedList<Integer> mtimeList; // 时间
    private volatile int mCapacity; // 容量
    private volatile int time;// 时长


    private static final class ValueHolder<KI, VI> {
        private final KI mKey; // 键不准许修改，若修改则用添加新的对象
        private VI mValue; // 值可以修改，用于更新

        private ValueHolder(KI key, VI value) {
            mKey = key;
            mValue = value;
        }
        
        
        /**
         * 回收Holder
         */
        private void recycle() {
            recycleKey();
            recycleValue();
        }

        private void recycleKey() {
            if (mKey instanceof IRecycleInterface) {
                ((IRecycleInterface) mKey).recycle();
            }
        }

        private void recycleValue() {
            if (mValue instanceof IRecycleInterface) {
                ((IRecycleInterface) mValue).recycle();
            }
        }
      }
    
    public interface IRecycleInterface {
        void recycle();
    }
        
        public MemoryCache() {
            this(CAPACITY_DEFAULT, TIME_DEFAULT);
        }

        public MemoryCache(int capacity, int time) {
            if (capacity < 0 || time < 0) 
            {
                throw new IllegalArgumentException("MemoryCache：构造函数参数错误");
            }
            mCacheList = new LinkedList<>();
            mcishuList = new LinkedList<>();
            mtimeList = new LinkedList<>();
            mCapacity = capacity;
            this.time = time*1000;
            }

        public synchronized void put(KO key, VO value) {
            if (key == null || value == null) {
                return;
            }
            
        	int times;
        	int currenttime = (int) System.currentTimeMillis();
        	
        	ListIterator<Integer> iterator = mtimeList.listIterator();
        	ListIterator<Integer> iterator2= mcishuList.listIterator();
        	ListIterator<ValueHolder<KO, VO>> iterator3= mCacheList.listIterator();
        	
        	while (iterator.hasNext()) {
        		times = iterator.next();
        		iterator2.next();
        		iterator3.next();
        	if(currenttime-times>time) {
        		iterator.remove();
        		iterator2.remove();
        		iterator3.remove();
        	}
        	}
        	
            if(mCacheList.size()<mCapacity) 
            {
            	mCacheList.addLast(new ValueHolder<>(key, value));
            	mcishuList.addLast(0);
            	mtimeList.addLast((int) System.currentTimeMillis());
            }else
            {
             	int cishu;
                int suoyin = 0,jieguo = 0;
                ListIterator<Integer> iterator4= mcishuList.listIterator();
                while (iterator4.hasNext()) {
              	if(iterator4.previousIndex()==1) {
                			jieguo= iterator4.next();
                		}
               		cishu = iterator4.next();
                		if(cishu<jieguo) {
                			suoyin=iterator4.previousIndex();
                			jieguo =cishu;
                		}
               	 }
                	mtimeList.remove(suoyin);
                	mcishuList.remove(suoyin);
                	mCacheList.remove(suoyin);
                	mCacheList.addLast(new ValueHolder<>(key, value));
                	mcishuList.addLast(0);
                	mtimeList.addLast((int) System.currentTimeMillis());	
                }
           }


        public synchronized VO get(KO key) {
            
                ListIterator<ValueHolder<KO, VO>> iterator = mCacheList.listIterator();
               while (iterator.hasNext()) {
                    ValueHolder<KO, VO> holder = iterator.next();
                   if (holder.mKey.equals(key)) { // 找到
                        return holder.mValue;
                        }
               }
            return null;
        }


        public synchronized void clear() {
        	mcishuList.clear();
        	mtimeList.clear();
            mCacheList.clear();
        }

        public synchronized int size() {
            return mCacheList.size();
        }

        public synchronized void remove(KO key) {
                ListIterator<ValueHolder<KO, VO>> iterator = mCacheList.listIterator();
            	ListIterator<Integer> iterator1= mtimeList.listIterator();
            	ListIterator<Integer> iterator2= mcishuList.listIterator();
                while (iterator.hasNext()) {
                    ValueHolder<KO, VO> holder = iterator.next();
                    iterator1.next();
                    iterator2.next();
                    if (holder.mKey.equals(key)) {
                        iterator.remove();
                        iterator1.remove();
                        iterator2.remove();
                        holder.recycle();
                        break;
                    }
                }
       }

        public synchronized void remove(int position) {
            if (position >= 0 && position < size()) {
                ValueHolder<KO, VO> removedHolder = mCacheList.remove(position);
                mtimeList.remove(position);
                mcishuList.remove(position);
                removedHolder.recycle();
            }
        }

        public void setCapacity(int capacity) {
            mCapacity = capacity;
        }


        public int getCapacity() {
            return mCapacity;
        }

     
        
}
