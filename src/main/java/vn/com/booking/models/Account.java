package vn.com.booking.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="accounts")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="account_id")
	private  Integer accountId ;
	@Column(name="username")
	private  String username ;
	@Column(name="password")
	private  String password ;
	@Column(name="role_id", columnDefinition = "integer default 1")
	private  Integer role;
	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private Profile profile;

	public Account() {
		this.role = 1;
	}
}
