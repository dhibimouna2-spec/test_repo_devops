package prestify.com.prestify.web.controllers;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for security-related exceptions.
 */
@ControllerAdvice
public class SecurityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex, Model model) {
        model.addAttribute("message", "Access Denied: You do not have permission to access this resource.");
        model.addAttribute("error", "403 - Forbidden");
        return "errors/access-denied";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        model.addAttribute("message", "An unexpected error occurred: " + ex.getMessage());
        model.addAttribute("error", "500 - Internal Server Error");
        return "errors/error";
    }
}
