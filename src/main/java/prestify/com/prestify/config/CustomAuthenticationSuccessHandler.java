package prestify.com.prestify.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import prestify.com.prestify.dao.entities.User;
import prestify.com.prestify.dao.entities.User.Role;

import java.io.IOException;

/**
 * Custom authentication success handler that redirects users based on their role
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                       HttpServletResponse response,
                                       Authentication authentication) throws IOException, ServletException {
        
        User user = (User) authentication.getPrincipal();
        Role role = user.getRole();
        
        String redirectUrl;
        
        switch (role) {
            case ADMIN:
                redirectUrl = "/dashboard";
                break;
            case SUPPLIER:
                redirectUrl = "/fournisseurindex";
                break;
            case CLIENT:
                redirectUrl = "/index";
                break;
            default:
                redirectUrl = "/";
        }
        
        response.sendRedirect(redirectUrl);
    }
}
