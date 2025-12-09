# Prestify Authentication and Security Implementation Guide

## Overview
This document describes the authentication and authorization system implemented in the Prestify Spring Boot application with role-based access control for Admin, Supplier, and Client users.

## Architecture

### User Roles
- **ADMIN**: Full system access, manages categories, offers, and complaints
- **SUPPLIER**: Can manage their own offers and view complaints
- **CLIENT**: Can browse and purchase services, file complaints

### Authentication Flow
1. User registers on `/signup` with selected role
2. User logs in on `/signin` with email/password
3. Spring Security validates credentials
4. User redirected to role-specific dashboard:
   - Admin → `/dashboard`
   - Supplier → `/fournisseurindex`
   - Client → `/index`

## Security Configuration

### Spring Security Setup
Located in: `src/main/java/prestify/com/prestify/config/SecurityConfig.java`

**Key Features:**
- BCryptPasswordEncoder for password encryption
- Form-based authentication
- CSRF protection
- Role-based authorization with @PreAuthorize annotations
- Custom success handler for role-based redirects
- Logout functionality

### Default Admin User
Auto-created on application startup (DataInitializer):
```
Email: admin@prestify.com
Password: admin123
Role: ADMIN
```

## Key Components

### 1. User Entity
`src/main/java/prestify/com/prestify/dao/entities/User.java`
- Implements `UserDetails` interface
- Enum Role: ADMIN, SUPPLIER, CLIENT
- Stores user credentials and personal information

### 2. UserService
`src/main/java/prestify/com/prestify/business/services/UserService.java`

**Methods:**
- `loadUserByUsername(String email)` - Load user by email
- `createUser(User user)` - Create new user with encrypted password
- `findByEmail(String email)` - Find user by email
- `existsByEmail(String email)` - Check email existence
- `existsByUsername(String username)` - Check username existence
- `createDefaultAdminIfNotExists()` - Auto-create admin on startup

### 3. AuthController
`src/main/java/prestify/com/prestify/web/controllers/AuthController.java`

**Endpoints:**
- `GET /signin` - Sign in form (with auto-redirect if already authenticated)
- `GET /signup` - Sign up form
- `POST /signup` - Handle registration with role selection
- `GET /index` - Home page

**Features:**
- Email and username duplication checks
- Role-based registration
- Automatic role-based redirects

### 4. AuthenticationUtils
`src/main/java/prestify/com/prestify/security/AuthenticationUtils.java`

**Utility Methods:**
```java
AuthenticationUtils.getCurrentUser()           // Get authenticated user
AuthenticationUtils.getCurrentUserEmail()      // Get current user's email
AuthenticationUtils.getCurrentUsername()       // Get current username
AuthenticationUtils.hasRole(Role role)         // Check if user has role
AuthenticationUtils.isAuthenticated()          // Check authentication status
AuthenticationUtils.isAdmin()                  // Check if admin
AuthenticationUtils.isSupplier()               // Check if supplier
AuthenticationUtils.isClient()                 // Check if client
```

## Protected Resources

### URL Pattern Authorization

| Path | Required Role | Description |
|------|---------------|-------------|
| `/`, `/index` | Public | Home page |
| `/signin`, `/signup` | Public | Authentication |
| `/css/**`, `/js/**`, `/images/**` | Public | Static resources |
| `/dashboard/**`, `/admin/**` | ADMIN | Admin dashboard |
| `/fournisseurindex/**` | SUPPLIER | Supplier dashboard |
| `/client/**` | CLIENT | Client pages |
| `/offers/**` | Any authenticated | Browse offers |
| `/reclamation/**` | Any authenticated | File complaints |

## Controller Security Annotations

### DashboardController
```java
@Controller
@PreAuthorize("hasRole('ADMIN')")
public class DashboardController { }
```

### OfferController
```java
@RequestMapping("/offers")
@PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'SUPPLIER')")
```

### ReclamationController
```java
@GetMapping("/list")
@PreAuthorize("hasRole('ADMIN')")

@GetMapping("/create")
@PreAuthorize("hasAnyRole('CLIENT', 'SUPPLIER')")

@PostMapping("/save")
@PreAuthorize("hasAnyRole('CLIENT', 'SUPPLIER')")
```

## Authentication Templates

### signin.html (`/authentification/signin.html`)
- Email and password form
- Form action: `/signin`
- Messages for login status (error, logout, registered)
- CSRF token included
- Thymeleaf integration

### signup.html (`/authentification/signup.html`)
- Registration form with all user fields
- Role selection dropdown (CLIENT/SUPPLIER)
- Form action: `/signup`
- Error/success messages
- CSRF token included
- Thymeleaf integration

### Error Pages
- `/errors/access-denied.html` - 403 Forbidden
- `/errors/error.html` - General 500 errors

## Configuration Files

### application.properties
```properties
# Security is enabled by default
# Default credentials (if using Spring Security defaults)
spring.security.user.name=admin
spring.security.user.password=admin123

# Logging for debugging
logging.level.org.springframework.security=DEBUG
```

## Usage Examples

### In Controllers
```java
@Controller
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @GetMapping("/dashboard")
    public String showDashboard() {
        // Only ADMIN can access
        return "admin/dashboard";
    }
}
```

### In Templates (Thymeleaf)
```html
<div th:if="${#authorization.expression('hasRole(\"ADMIN\")')}">
    Admin-only content
</div>

<div th:if="${#authorization.expression('hasRole(\"SUPPLIER\")')}">
    Supplier-only content
</div>
```

### In Services
```java
@Service
public class MyService {
    
    public void doSomething() {
        User currentUser = AuthenticationUtils.getCurrentUser();
        if (AuthenticationUtils.isAdmin()) {
            // Admin logic
        }
    }
}
```

## Security Best Practices Implemented

✅ **Password Encryption** - BCryptPasswordEncoder with salt
✅ **CSRF Protection** - CSRF token in forms
✅ **Session Management** - Secure session handling
✅ **Access Control** - Method-level security with @PreAuthorize
✅ **SQL Injection Prevention** - JPA parameterized queries
✅ **User Validation** - Email/username uniqueness checks
✅ **Error Handling** - Custom exception handlers
✅ **Logout Security** - Session invalidation and cookie deletion

## Testing Credentials

### Admin User
- Email: `admin@prestify.com`
- Password: `admin123`
- Role: ADMIN

### Create Test Users
Register via `/signup` with:
- Role: CLIENT → Can browse offers and file complaints
- Role: SUPPLIER → Can manage offers

## Troubleshooting

### Cannot login
1. Check if user exists in database
2. Verify password is correct (case-sensitive)
3. Check if user account is enabled
4. Clear browser cache and cookies

### Access Denied errors
1. Verify user has required role
2. Check @PreAuthorize annotations on controller
3. Review SecurityConfig URL patterns
4. Check SecurityContextHolder has authentication

### CSRF errors
1. Ensure form includes CSRF token: `<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}" />`
2. POST requests must include valid CSRF token
3. Check CSRF is not disabled in SecurityConfig

## Future Enhancements

- [ ] JWT Token-based authentication
- [ ] OAuth2 integration
- [ ] Two-factor authentication
- [ ] Password reset functionality
- [ ] Account lockout after failed attempts
- [ ] Audit logging
- [ ] Rate limiting
- [ ] Remember-me functionality

## Dependencies

Required in `pom.xml`:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## Contact & Support

For issues or questions about authentication, refer to Spring Security documentation:
https://spring.io/projects/spring-security
