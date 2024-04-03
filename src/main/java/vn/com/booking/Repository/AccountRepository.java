package vn.com.booking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.booking.models.Account;


@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
	Account findByUsername(String username);
}
