package prestify.com.prestify.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import prestify.com.prestify.dao.entities.User;
import prestify.com.prestify.dao.entities.User.Role;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findByRole(Role role);
}