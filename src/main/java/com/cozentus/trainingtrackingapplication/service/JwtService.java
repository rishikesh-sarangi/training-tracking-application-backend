package com.cozentus.trainingtrackingapplication.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

	private static final String SECRET = "6DE68F53F1C08B7F593ADBFFA8AD3C0859FF25B1A0627EAD33AF3CDA93CBD06AEB7BCF0E1D197797CB0578A00A086A7F58A925EFA4F293E1F91B236FA35AB34C";
	private static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

	public String generateToken(UserDetails userDetails) {
		Map<String, String> claims = new HashMap<>();
		claims.put("name", "happy");
		return Jwts.builder().claims(claims).subject(userDetails.getUsername()).issuedAt(Date.from(Instant.now()))
				.expiration(Date.from(Instant.now().plusMillis(VALIDITY))).signWith(generateKey()).compact();
	}

	private SecretKey generateKey() {
		byte[] decodedKey = Base64.getDecoder().decode(SECRET);
		return Keys.hmacShaKeyFor(decodedKey);
	}

	public String extractUsername(String jwt) {
		Claims claims = getClaims(jwt);
		return claims.getSubject();
	}

	private Claims getClaims(String jwt) {
		return Jwts.parser().verifyWith(generateKey()).build().parseSignedClaims(jwt).getPayload();
	}

	public boolean isTokenValid(String jwt) {
		Claims claims = getClaims(jwt);
		return claims.getExpiration().after(Date.from(Instant.now()));
	}
}
