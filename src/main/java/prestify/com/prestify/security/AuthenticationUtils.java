package prestify.com.prestify.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import prestify.com.prestify.dao.entities.User;

/**
 * Utility class for authentication and authorization operations.
 */
public class AuthenticationUtils {

    private AuthenticationUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Get the currently authenticated user.
     *
     * @return the authenticated User or null if not authenticated
     */
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && 
            !authentication.getName().equals("anonymousUser")) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return (User) principal;
            }
        }
        return null;
    }

    /**
     * Get the current user's email.
     *
     * @return the email of the authenticated user or null
     */
    public static String getCurrentUserEmail() {
        User user = getCurrentUser();
        return user != null ? user.getEmail() : null;
    }

    /**
     * Get the current user's username.
     *
     * @return the username of the authenticated user or null
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : null;
    }

    /**
     * Check if the current user has a specific role.
     *
     * @param role the role to check
     * @return true if the user has the role, false otherwise
     */
    public static boolean hasRole(User.Role role) {
        User user = getCurrentUser();
        return user != null && user.getRole() == role;
    }

    /**
     * Check if the current user is authenticated.
     *
     * @return true if authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && 
               !authentication.getName().equals("anonymousUser");
    }

    /**
     * Check if the current user is an admin.
     *
     * @return true if the user is an admin
     */
    public static boolean isAdmin() {
        return hasRole(User.Role.ADMIN);
    }

    /**
     * Check if the current user is a supplier.
     *
     * @return true if the user is a supplier
     */
    public static boolean isSupplier() {
        return hasRole(User.Role.SUPPLIER);
    }

    /**
     * Check if the current user is a client.
     *
     * @return true if the user is a client
     */
    public static boolean isClient() {
        return hasRole(User.Role.CLIENT);
    }
}
