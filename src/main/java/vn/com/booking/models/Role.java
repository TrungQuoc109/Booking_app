package vn.com.booking.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "role")
@Data
public class Role {
	@Id
	@GeneratedValue(generator = "UUID")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID roleId;

	@Column(name = "role_name")
	private String roleName;

	@OneToMany(mappedBy = "role")
	private List<RolePermission> rolePermissionMappings;

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Account> accountsList;

	public Role(String roleName) {
		this.roleName = roleName;
	}

	public Role() {
	}

	@Override
	public String toString() {
		return "Role{" +
				"roleId=" + roleId +
				", roleName='" + roleName + '\'' +
				'}';
	}
}
