package vn.com.booking.models;

import jakarta.persistence.*;
import lombok.Data;
import vn.com.booking.enumclass.Role;

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
	@Column(name="role_id")
	private Integer role;
	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private Profile profile;

	public Account(String username, String password)
	{
		this.username = username;
		this.password = password;
		this.role = Role.User.getValue();
	}
	public Account(){
		this.role = Role.User.getValue();
	}

	public Account(Integer accountId, String username, String password, Integer role) {
		this.accountId = accountId;
		this.username = username;
		this.password = password;
		this.role = role;
	}
}
