package vn.com.booking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.booking.models.Role;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByRoleName(String roleName);

	Role findByRoleId(UUID roleId);
}
