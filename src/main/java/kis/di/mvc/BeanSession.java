package kis.di.mvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author naoki
 */
public class BeanSession {
    private ThreadLocal<String> sessionId = new InheritableThreadLocal<>();
    private Map<String, Map<String, Object>> beans = new HashMap<>();
    
    public void setSessionId(String id) {
        sessionId.set(id);
        beans.put(id, new HashMap<>());
    }
    
    public Optional<Object> getBean(String name) {
        return Optional.ofNullable(beans.get(sessionId.get()).get(name));
    }
    
    public void register(String name, Object bean) {
        beans.get(sessionId.get()).put(name, bean);
    }
}
