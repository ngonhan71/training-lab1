package com.tma.utils;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import com.tma.controller.AuthController;
import com.tma.model.entity.user.AppUser;
import com.tma.enums.TokenTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Value(value = "${jwt.secret-key}")
	private String secretKey;
	
	@Value(value = "${jwt.expiration-accessToken}")
	private long accessTokenExpiration;

	@Value(value = "${jwt.expiration-refreshToken}")
	private long refreshTokenExpiration;

	@Value(value = "${jwt.tokenPrefix}")
	private String tokenPrefix;

	@Autowired
	private RedisTemplate redisTemplate;
	
	public String getUsername(String token) {
		return getClaim(token, Claims::getSubject);
	}
	
	public Date getExpiration(String token) {
		return getClaim(token, Claims::getExpiration);
	}
	
	public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims getAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}
	
	public boolean isTokenExpired(String token) {
		final Date expiration = getExpiration(token);
		return expiration.before(new Date());
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsername(token);
		try {
			if (redisTemplate.hasKey("revoked_jwt:" + token)) {
				return false;
			}
			return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
		} catch (Exception ex) {
			logger.error("Redis error: " + ex.getMessage());
		}
		return false;
	}

	public String generateToken(AppUser appUser, TokenTypes tokenTypes) {

		long tokenExpirationTime = tokenTypes.equals(tokenTypes.ACCESS_TOKEN) ? accessTokenExpiration : refreshTokenExpiration;

		String email = appUser.getEmail();

		String token = Jwts.builder().setSubject(email)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * tokenExpirationTime))
				.claim("type", tokenTypes)
				.claim("email", email)
				.signWith(SignatureAlgorithm.HS256, secretKey).compact();

		try {
			redisTemplate.opsForValue().set(tokenPrefix + token, "true", tokenExpirationTime, TimeUnit.SECONDS);
		} catch (Exception ex) {
			logger.error("Failed to cache JWT Token: " + ex.getMessage());
		}

		return token;
	}

	public void blacklistToken(String token) {
		try {
			Long remainingTtlSeconds = redisTemplate.getExpire(tokenPrefix + token);
			if (remainingTtlSeconds != null) {
				redisTemplate.opsForValue().set("revoked_jwt:" + token, true, Duration.ofSeconds(remainingTtlSeconds));
			}
		} catch (Exception ex) {
			logger.error("Failed to revoke JWT Token: " + ex.getMessage());
		}
	}

}
