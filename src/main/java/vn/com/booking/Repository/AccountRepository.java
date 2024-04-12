package vn.com.booking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.booking.models.Account;

import java.util.List;
import java.util.UUID;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	Account findByUsername(String username);

	Account findByAccountId(UUID accountId);

	Account findByPhoneNumber(String phoneNumber);

	List<Account> findAllByRoleRoleName(String roleName);

}
