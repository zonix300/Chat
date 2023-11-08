package zonix.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zonix.chat.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
