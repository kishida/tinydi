package kis.di.sample;

import java.time.LocalDateTime;

import javax.inject.Named;

import kis.di.annotation.RequestScoped;

/**
 *
 * @author naoki
 */
@RequestScoped
@Named
public class Now {
    private LocalDateTime time;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    
}
