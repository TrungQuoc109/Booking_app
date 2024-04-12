package vn.com.booking.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "rolepermission")
public class RolePermission {
	@Id
	@GeneratedValue(generator = "UUID")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID rpmId;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	@ManyToOne
	@JoinColumn(name = "permission_id")
	private Permission permission;

	public RolePermission() {
	}

	@Override
	public String toString() {
		return "RolePermission{" +
				"rpmId=" + rpmId +
				", role=" + role.getRoleName() +
				", permission=" + permission.getPermissionName() +
				'}';
	}
}
