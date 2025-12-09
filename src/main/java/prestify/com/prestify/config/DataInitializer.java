// src/main/java/prestify/com/prestify/config/DataInitializer.java
package prestify.com.prestify.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import prestify.com.prestify.business.services.UserService;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    
    private final UserService userService;
    
    @PostConstruct
    public void init() {
        userService.createDefaultAdminIfNotExists();
    }
}