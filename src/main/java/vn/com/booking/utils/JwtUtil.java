package vn.com.booking.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import vn.com.booking.models.Account;

import javax.crypto.SecretKey;
import java.util.Date;

	@Component
	public class JwtUtil {

		//@Value("${jwt.secret}")
		private final String jwtSecret = "12345678901234567890123456789012";
		//@Value("${jwt.expiration}")
		public  Integer jwtExpirationMs = 21600000;


		public String generateJwtToken(Account account) {
			SecretKey key = Keys.hmacShaKeyFor(this.jwtSecret.getBytes());
			JwtBuilder builder = Jwts.builder()
					.setSubject(account.getUsername())
					.claim("role", account.getRole())
					.claim("accountId", account.getAccountId())
					.setIssuedAt(new Date())
					.setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
					.signWith(key);

			return builder.compact();
		}

		public Integer getAccountIdFromJwtToken(String token) {
			try {
				if (token.startsWith("Bearer ")) {
					token = token.substring(7);
				}
				SecretKey key = Keys.hmacShaKeyFor(this.jwtSecret.getBytes());
				JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
				Claims claims = jwtParser.parseClaimsJws(token).getBody();
				return (Integer) claims.get("accountId");

			} catch (JwtException | ClassCastException e) {
				System.out.println("Error parsing JWT token: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		public Integer getRoleFromJwtToken(String token) {
			try {
				if (token.startsWith("Bearer ")) {
					token = token.substring(7);
				}
				SecretKey key = Keys.hmacShaKeyFor(this.jwtSecret.getBytes());
				JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
				Claims claims = jwtParser.parseClaimsJws(token).getBody();
				return (Integer) claims.get("role");
			} catch (JwtException | ClassCastException e) {
				System.out.println("Error parsing JWT token: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		}


		public boolean validateJwtToken(String authToken) {
			try {
				if (authToken.startsWith("Bearer ")) {
					authToken = authToken.substring(7);
				}
				SecretKey key = Keys.hmacShaKeyFor(this.jwtSecret.getBytes());
				JwtParser jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
				jwtParser.parseClaimsJws(authToken);
				return true;
			} catch (JwtException | IllegalArgumentException e) {
				System.out.println("Error parsing JWT token: " + e.getMessage());
				e.printStackTrace();
				return false;
			}
		}



	}
