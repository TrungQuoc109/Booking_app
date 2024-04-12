package vn.com.booking.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "permission")
@Data
public class Permission {
	@Id
	@GeneratedValue(generator = "UUID")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID permissionId;
	@Column(name = "permission_name")
	private String permissionName;
	@OneToMany(mappedBy = "permission", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RolePermission> rolePermissionMappings;

	@Override
	public String toString() {
		return "Permission{" +
				"permissionId=" + permissionId +
				", permissionName='" + permissionName + '\'' +
				'}';
	}
}
