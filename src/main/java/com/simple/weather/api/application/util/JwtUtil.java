package com.simple.weather.api.application.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil
{
	private final MacAlgorithm algorithm = (MacAlgorithm) Jwts.SIG.HS512;
	
	public String generateToken(String id, String secret)
	{
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
		
		return Jwts.builder()
				.subject(id)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
				.signWith(key, algorithm)
				.compact();
	}
	
	public Claims validateToken(String token, String secret)
	{
		Claims claim = null;
		
		try
		{
			SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
			claim = Jwts.parser()
					.verifyWith(key)
					.build()
					.parseSignedClaims(token)
					.getPayload();
		}
		catch (Exception e)
		{
			log.error("Error verify jwt token: {}", e.getMessage(), e);
		}
		
		return claim;
	}
}
