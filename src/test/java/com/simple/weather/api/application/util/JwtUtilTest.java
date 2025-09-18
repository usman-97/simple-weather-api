package com.simple.weather.api.application.util;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;

class JwtUtilTest
{
	private JwtUtil jwtUtil;
	private String secret;
	private String invalidSecret;
	private String subjectId;
	private MacAlgorithm algorithm;
	
	@BeforeEach
	void setUp()
	{
		jwtUtil = new JwtUtil();
		algorithm = Jwts.SIG.HS512;
		
		// The new, non-deprecated way to generate a key
		SecretKey key = algorithm.key().build();
		secret = Encoders.BASE64.encode(key.getEncoded());
		
		// An invalid secret for negative testing
		SecretKey invalidKey = algorithm.key().build();
		invalidSecret = Encoders.BASE64.encode(invalidKey.getEncoded());
		
		subjectId = "test-user-123";
	}
	
	@Test
	@DisplayName("Should generate a token and validate it successfully")
	void testGenerateToken_shouldGenerateAndValidateTokenSuccessfully()
	{
		// Given
		String token = jwtUtil.generateToken(subjectId, secret);
		
		// When
		Claims claims = jwtUtil.validateToken(token, secret);
		
		// Then
		assertNotNull(claims);
		assertEquals(subjectId, claims.getSubject());
	}
	
	@Test
	@DisplayName("Should return null for a token signed with an invalid secret")
	void testGenerateToken_shouldReturnNullForInvalidSecret()
	{
		// Given
		String token = jwtUtil.generateToken(subjectId, secret);
		
		// When
		Claims claims = jwtUtil.validateToken(token, invalidSecret);
		
		// Then
		assertNull(claims);
	}
	
	@Test
	@DisplayName("Should return null for an expired token")
	void testValidateToken_shouldReturnNullForExpiredToken()
	{
		// Given
		// Manually create an expired token
		MacAlgorithm algorithm = (MacAlgorithm) Jwts.SIG.HS512;
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		
		String expiredToken = Jwts.builder()
				.subject(subjectId)
				.issuedAt(new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(2)))
				.expiration(new Date(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(1)))
				.signWith(key, algorithm)
				.compact();
		
		// When
		Claims claims = jwtUtil.validateToken(expiredToken, secret);
		
		// Then
		assertNull(claims);
	}
	
	@Test
	@DisplayName("Should return null for a malformed token string")
	void testValidateToken_shouldReturnNullForMalformedToken()
	{
		// Given
		String malformedToken = "this.is.not.a.valid.jwt";
		
		// When
		Claims claims = jwtUtil.validateToken(malformedToken, secret);
		
		// Then
		assertNull(claims);
	}
	
	@Test
	@DisplayName("Should return null for a token with an invalid signature")
	void testValidateToken_shouldReturnNullForInvalidSignature()
	{
		// Given
		// Generate a valid token
		String token = jwtUtil.generateToken(subjectId, secret);
		
		// Tamper with the token's signature to invalidate it
		String tamperedToken = token.substring(0, token.lastIndexOf('.')) + ".invalid_signature_part";
		
		// When
		Claims claims = jwtUtil.validateToken(tamperedToken, secret);
		
		// Then
		assertNull(claims);
	}
	
	@Test
	@DisplayName("Should contain correct claims in a generated token")
	void testValidateToken_shouldContainCorrectClaimsInGeneratedToken()
	{
		// Given
		String token = jwtUtil.generateToken(subjectId, secret);
		
		// When
		Claims claims = jwtUtil.validateToken(token, secret);
		
		// Then
		assertNotNull(claims);
		assertEquals(subjectId, claims.getSubject());
		assertNotNull(claims.getIssuedAt());
		assertNotNull(claims.getExpiration());
	}
}
