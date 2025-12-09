package prestify.com.prestify.web.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import prestify.com.prestify.business.services.UserService;
import prestify.com.prestify.dao.entities.User;
import prestify.com.prestify.dao.entities.User.Role;

@Controller
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;

    /**
     * Home page - Public page for everyone
     */
    @GetMapping("/")
    public String home() {
        // Just show the public home page
        return "index/index";
    }

    /**
     * Sign in page - Shows form if not authenticated, redirects if already authenticated
     */
    @GetMapping("/signin")
    public String signin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            // Already authenticated, redirect to dashboard
            User user = (User) auth.getPrincipal();
            switch (user.getRole()) {
                case ADMIN:
                    return "redirect:/dashboard";
                case SUPPLIER:
                    return "redirect:/fournisseurindex";
                case CLIENT:
                    return "redirect:/index";
            }
        }
        // Not authenticated, show login form
        return "authentification/signin";
    }

    /**
     * Client dashboard - Protected, only for authenticated clients
     * Called after successful login for CLIENT role
     */
    @GetMapping("/index")
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            User user = (User) auth.getPrincipal();
            model.addAttribute("currentUser", user);
            return "client/dashboard";
        }
        return "redirect:/signin";
    }

    /**
     * Sign up page
     */
    @GetMapping("/signup")
    public String signup() {
        return "authentification/signup";
    }

    /**
     * Handle user registration
     */
    @PostMapping("/signup")
    public String handleSignUp(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam(defaultValue = "CLIENT") String role,
                             Model model) {
        try {
            // Check if user already exists
            if (userService.existsByEmail(email)) {
                model.addAttribute("error", "Email already registered");
                return "authentification/signup";
            }
            
            if (userService.existsByUsername(username)) {
                model.addAttribute("error", "Username already taken");
                return "authentification/signup";
            }
            
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            
            // Set role based on parameter
            try {
                user.setRole(Role.valueOf(role.toUpperCase()));
            } catch (IllegalArgumentException e) {
                user.setRole(Role.CLIENT); // Default to CLIENT if invalid role
            }
            
            userService.createUser(user);
            return "redirect:/signin?registered=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "authentification/signup";
        }
    }
}