# Prestify - Quick Start Guide

## 🚀 Getting Started with Authentication

### Prerequisites
- Java 17+
- MySQL 8.0+
- Maven 3.6+

### Step 1: Database Setup

Create a MySQL database:
```sql
CREATE DATABASE prestify22;
```

Update credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/prestify22
spring.datasource.username=root
spring.datasource.password=your_password
```

### Step 2: Start the Application

```bash
cd c:\Users\LOQ\Downloads\Prestify_Plateforme_de_gestion_des_services-main
mvn spring-boot:run
```

Or from VS Code terminal:
```powershell
.\mvnw.cmd spring-boot:run
```

Application will be available at: `http://localhost:8093`

### Step 3: Access the Application

1. **Home Page**: `http://localhost:8093/`
2. **Sign In**: `http://localhost:8093/signin`
3. **Sign Up**: `http://localhost:8093/signup`

---

## 👤 Default Admin Account

Automatically created on first startup:

| Field | Value |
|-------|-------|
| **Email** | admin@prestify.com |
| **Password** | admin123 |
| **Role** | ADMIN |

### Login as Admin
1. Go to `http://localhost:8093/signin`
2. Enter email: `admin@prestify.com`
3. Enter password: `admin123`
4. Click "Sign in"
5. Redirected to `/dashboard`

---

## 📝 Create Test Users

### Create a Client User
1. Go to `http://localhost:8093/signup`
2. Fill in the form:
   - Username: `client1`
   - First Name: `John`
   - Last Name: `Doe`
   - Email: `client@example.com`
   - Password: `client123`
   - Account Type: **Client**
3. Click "Sign Up"
4. You'll be redirected to sign in
5. Login with your client credentials
6. Redirected to `/index`

### Create a Supplier User
1. Go to `http://localhost:8093/signup`
2. Fill in the form:
   - Username: `supplier1`
   - First Name: `Jane`
   - Last Name: `Smith`
   - Email: `supplier@example.com`
   - Password: `supplier123`
   - Account Type: **Supplier**
3. Click "Sign Up"
4. You'll be redirected to sign in
5. Login with your supplier credentials
6. Redirected to `/fournisseurindex`

---

## 🔒 Protected Routes

### Admin Routes (requires ADMIN role)
- `/dashboard` - Admin dashboard
- `/admin/**` - Admin operations
- `/adminofferslist` - View all offers
- `/manage/**` - Admin management

### Supplier Routes (requires SUPPLIER role)
- `/fournisseurindex` - Supplier dashboard
- `/supplier/**` - Supplier operations
- `/offer/**` - Offer management

### Client Routes (requires CLIENT role)
- `/client/**` - Client operations

### Authenticated Routes (any logged-in user)
- `/offers/**` - Browse offers
- `/reclamation/**` - File complaints

### Public Routes (no authentication required)
- `/` - Home
- `/signin` - Login page
- `/signup` - Registration page
- `/AboutUs` - About page
- `/static/**` - CSS, JS, Images

---

## 🧪 Testing Scenarios

### Scenario 1: Admin Access
```
1. Login as admin@prestify.com / admin123
2. Visit /dashboard → Should see admin dashboard
3. Visit /adminofferslist → Should see all offers
4. Visit /client → Should get 403 Access Denied
```

### Scenario 2: Client Access
```
1. Create and login as client user
2. Visit /index → Should see client home
3. Visit /offers → Should see offers list
4. Visit /dashboard → Should get 403 Access Denied
```

### Scenario 3: Supplier Access
```
1. Create and login as supplier user
2. Visit /fournisseurindex → Should see supplier dashboard
3. Visit /offer → Should see supplier offers
4. Visit /admin → Should get 403 Access Denied
```

### Scenario 4: Logout
```
1. Click logout link or visit /logout
2. Should be redirected to /signin with logout message
3. Session should be invalidated
```

---

## 🛠️ Configuration

### Change Admin Password
1. Login as admin
2. Update the password in your service layer
3. Database will auto-update with encrypted password

### Adjust Session Timeout
Add to `application.properties`:
```properties
server.servlet.session.timeout=30m
```

### Enable/Disable Features

**Disable CSRF (not recommended for production):**
```java
// In SecurityConfig.java
.csrf(csrf -> csrf.disable())
```

**Change password encoder:**
```java
// In SecurityConfig.java
@Bean
public PasswordEncoder passwordEncoder() {
    return new Pbkdf2PasswordEncoder();
}
```

---

## 📊 Database Schema

### Users Table
Auto-created by JPA:
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    role ENUM('ADMIN', 'SUPPLIER', 'CLIENT'),
    enabled BOOLEAN DEFAULT true
);
```

---

## 🐛 Troubleshooting

### Issue: Cannot login
**Solution:**
- Check database connection
- Verify user exists in database
- Check password is correct (case-sensitive)
- Try clearing browser cookies

### Issue: 403 Access Denied
**Solution:**
- Verify user has required role
- Check security configuration
- Ensure @PreAuthorize annotations are correct
- Try restarting application

### Issue: CSRF token error
**Solution:**
- Ensure form includes CSRF token
- Verify POST requests include token
- Check CSRF is not disabled

### Issue: Session issues
**Solution:**
- Clear browser cache and cookies
- Check session timeout configuration
- Restart application
- Check database connection

---

## 📚 Documentation Files

1. **AUTHENTICATION_GUIDE.md** - Detailed authentication guide
2. **IMPLEMENTATION_SUMMARY.md** - Summary of all changes
3. **This file** - Quick start guide

---

## 🔗 Useful Links

- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Thymeleaf Documentation](https://www.thymeleaf.org/)
- [MySQL Documentation](https://dev.mysql.com/doc/)

---

## ✅ Checklist for Production

- [ ] Change default admin password
- [ ] Enable HTTPS/SSL
- [ ] Configure email service for password reset
- [ ] Set up audit logging
- [ ] Configure firewall rules
- [ ] Enable rate limiting
- [ ] Set up backup strategy
- [ ] Configure monitoring and alerts
- [ ] Test with production-like data
- [ ] Review security settings
- [ ] Configure logging appropriately
- [ ] Set up CI/CD pipeline

---

## 📞 Support

For issues or questions:
1. Check AUTHENTICATION_GUIDE.md for detailed information
2. Review Spring Security documentation
3. Check application logs for error messages
4. Verify database connection and schema

---

**Happy coding! Your Prestify authentication system is ready!** 🎉
