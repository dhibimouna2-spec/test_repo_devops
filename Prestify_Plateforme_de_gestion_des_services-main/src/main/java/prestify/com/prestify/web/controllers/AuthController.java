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

    @GetMapping("/signin")
    public String signin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            User user = (User) auth.getPrincipal();
            return redirectBasedOnRole(user.getRole());
        }
        return "authentification/signin";
    }

    @GetMapping("/signup")
    public String signup() {
        return "authentification/signup";
    }

    @PostMapping("/signup")
    public String handleSignUp(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             Model model) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRole(Role.CLIENT); // Default role for signup
            userService.createUser(user);
            return "redirect:/signin?registered=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "authentification/signup";
        }
    }

    @GetMapping("/index")
    public String index() {
        return "index/index";
    }

    private String redirectBasedOnRole(Role role) {
        return switch (role) {
            case ADMIN -> "redirect:/dashboard";
            case SUPPLIER -> "redirect:/fournisseurindex";
            case CLIENT -> "redirect:/index";
        };
    }
}