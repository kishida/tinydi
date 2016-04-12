package kis.di.mvc;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author naoki
 */
public class BeanSession {
    private final ThreadLocal<String> sessionId = new InheritableThreadLocal<>();
    private final Map<String, Map<String, Object>> beans = new HashMap<>();
    
    public void setSessionId(String id) {
        sessionId.set(id);
        if (!beans.containsKey(id)) {
            beans.put(id, new HashMap<>());
        }
    }
    
    public Map<String, Object> getBeans() {
        return beans.get(sessionId.get());
    }

    public boolean isSessionRegistered(String id) {
        return beans.containsKey(id);
    }
    
}
