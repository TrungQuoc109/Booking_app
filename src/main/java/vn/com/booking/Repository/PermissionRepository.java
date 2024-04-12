package vn.com.booking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.booking.models.RolePermission;

import java.util.UUID;

@Repository
public interface PermissionRepository extends JpaRepository<RolePermission, Long> {

	RolePermission findRolePermissionByRole_RoleIdAndPermissionPermissionName(UUID roleName, String permissionName);
	//SELECT rp.*
	//FROM RolePermission rp
	//JOIN Role r ON rp.role_id = r.id
	//JOIN Permission p ON rp.permission_id = p.id
	//WHERE r.role_name = :roleName
	//AND p.permission_name = :permissionName;


}
