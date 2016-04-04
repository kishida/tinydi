package kis.di;

/**
 *
 * @author naoki
 */
public class LocalCache<T> {
    private ThreadLocal<T> local = new ThreadLocal<>();
    private String name;

    public LocalCache(String name) {
        this.name = name;
    }
    
    public T get() {
        T obj = local.get();
        if (obj != null) {
            return obj;
        }
        obj = (T) Context.getBean(name);
        local.set(obj);
        return obj;
    }
}
