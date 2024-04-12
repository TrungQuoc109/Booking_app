package vn.com.booking;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookingApplicationTests {

	@Test
	void contextLoads() {
		assertEquals(42, Integer.sum(19, 23));
	}

}
