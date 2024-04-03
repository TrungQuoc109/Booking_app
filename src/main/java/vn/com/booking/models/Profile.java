package vn.com.booking.models;


import jakarta.persistence.*;
import lombok.Data;



@Entity
@Data
@Table(name="profiles")
public class Profile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="profile_id")
	private  Integer profileId ;
	@Column(name="fullname")
	private  String name ;
	@Column(name="email")
	private  String email ;
	@Column(name="number_phone")
	private  String numberPhone;
	@Column(name="emergency_contact")
	private  String emergencyContact;
	@Column(name="age")
	private  Integer age;
	@OneToOne
	@JoinColumn(name = "account_id", referencedColumnName = "account_id")
	private Account account;
}
