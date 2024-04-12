package vn.com.booking.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "account")
public class Account {
	@Id
	@GeneratedValue(generator = "UUID")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID accountId;
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "name")
	private String name;
	@Column(name = "email")
	private String email;
	@Column(name = "phone_number")
	private String phoneNumber;
	@Column(name = "age")
	private Integer age;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;


	public Account(String username, String password) {
		this.username = username;
		this.password = password;

	}

	public Account() {

	}

	public Account(String username, String password, String name, String email, String phoneNumber, Integer age, Role role) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.age = age;
		this.role = role;
	}

	@Override
	public String toString() {
		return "Account{" +
				"accountId=" + accountId +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", age=" + age +
				'}';
	}
}
