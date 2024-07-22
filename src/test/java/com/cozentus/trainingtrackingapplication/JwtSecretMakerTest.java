package com.cozentus.trainingtrackingapplication;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;

class JwtSecretMakerTest {
	@Test
	@Disabled
	void generateSecretKey() {
		SecretKey key = Jwts.SIG.HS512.key().build();
		String encodedKey = DatatypeConverter.printHexBinary(key.getEncoded());
		System.out.printf("\nKey = [%s]\n", encodedKey);
	}
}
