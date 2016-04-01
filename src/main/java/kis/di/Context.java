package kis.di;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

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
                return createObject(type);
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new RuntimeException(name + " can not instanciate", ex);
            }
        });
    }
    
    private static <T> T createObject(Class<T> type) throws InstantiationException, IllegalAccessException {
        T object = type.newInstance();
        for (Field field : type.getDeclaredFields()) {
            Inject inject = field.getAnnotation(Inject.class);
            if (inject == null) {
                continue;
            }
            field.setAccessible(true);
            field.set(object, getBean(field.getName()));
        }
        return object;
    }
    
}
