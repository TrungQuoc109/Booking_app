package vn.com.booking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.booking.models.Profile;

import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long> {
	Profile findByAccount_AccountId(Integer accountId);
	List <Profile> findAllByAccount_Role(Integer role);
}
