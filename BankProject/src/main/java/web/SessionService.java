package web;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import web.pojo.User;
import web.pojo.UserInfo;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by zdoba on 04.12.2018.
 */
public class SessionService {

    private static final int CACHE_SIZE = 10;

    private Map<String, UserInfo> userCache;

    private static SessionService instance;

    private SessionService(){
        userCache = Collections.synchronizedMap(new LruCache<String, UserInfo>(CACHE_SIZE));
    }

    public static SessionService getInstance(){
        if (instance == null){
            synchronized (SessionService.class){
                if (instance == null){
                    instance = new SessionService();
                }
            }
        }
        return instance;
    }

    public boolean contains(String sessionId){
        return userCache.containsKey(sessionId);
    }

    public String createUserSession(String clientId){
        String token = UUID.randomUUID().toString();
        userCache.put(token,new UserInfo(clientId));
        return token;
    }

    public UserInfo getUserSession(String token){
        return userCache.get(token);
    }

    public void endUserSession(String token){
        userCache.remove(token);
    }

    private class LruCache<A, B> extends LinkedHashMap<A, B> {
        private final int maxEntries;

        public LruCache(final int maxEntries) {
            super(maxEntries + 1, 1.0f, true);
            this.maxEntries = maxEntries;
        }

        /**
         * Returns <tt>true</tt> if this <code>LruCache</code> has more entries than the maximum specified when it was
         * created.
         *
         * <p>
         * This method <em>does not</em> modify the underlying <code>Map</code>; it relies on the implementation of
         * <code>LinkedHashMap</code> to do that, but that behavior is documented in the JavaDoc for
         * <code>LinkedHashMap</code>.
         * </p>
         *
         * @param eldest
         *            the <code>Entry</code> in question; this implementation doesn't care what it is, since the
         *            implementation is only dependent on the size of the cache
         * @return <tt>true</tt> if the oldest
         * @see java.util.LinkedHashMap#removeEldestEntry(Map.Entry)
         */
        @Override
        protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
            return super.size() > maxEntries;
        }
    }
}
