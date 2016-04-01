package kis.di;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author naoki
 */
public class Context {
    
    static Map<String, Class> types = new HashMap<>();
    static Map<String, Object> beans = new HashMap<>();

    public static void register(String name, Class type) {
        types.put(name, type);
    }

    public static Object getBean(String name) {
        return beans.computeIfAbsent(name, (java.lang.String key) -> {
            Class type = types.get(name);
            Objects.requireNonNull(type, name + " not found.");
            try {
                return type.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException(name + " can not instanciate", ex);
            }
        });
    }
    
}
