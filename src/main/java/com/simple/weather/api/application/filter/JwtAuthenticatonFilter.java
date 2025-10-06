package com.simple.weather.api.application.filter;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.simple.weather.api.application.model.entity.ApiUser;
import com.simple.weather.api.application.service.ApiUserService;
import com.simple.weather.api.application.util.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticatonFilter extends OncePerRequestFilter
{
	private final JwtUtil jwtUtil;
	private final ApiUserService apiUserService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain filterChain)
			throws ServletException, IOException
	{
		String path = req.getServletPath();
		if (path.startsWith("/v1/auth"))
		{
			filterChain.doFilter(req, resp);
			return;
		}
		
		String clientId = req.getHeader("X-Client-Id");
		String authHeader = req.getHeader("Authorization");
		if (clientId == null || !StringUtils.hasText(authHeader))
		{
			log.info("Authentication failure, missing required header.");
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		if (!authHeader.startsWith("Bearer "))
		{
			log.info("Authentication failure, missing required header.");
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		String token = authHeader.substring(7);
		if (!StringUtils.hasText(token))
		{
			log.info("Authentication failure, invalid token");
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		ApiUser user = apiUserService.fetchApiUser(clientId);
		if (user == null)
		{
			log.info("Authentication failure, invalid client id");
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		Claims claim = jwtUtil.validateToken(token, user.getClientSecret());
		if (claim == null)
		{
			log.info("Authentication failure, failed to verify token");
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		String verifiedClientId = claim.getSubject();
		if (StringUtils.hasText(verifiedClientId) && SecurityContextHolder.getContext().getAuthentication() == null)
		{
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					new User(verifiedClientId, "", Collections.singletonList(new SimpleGrantedAuthority("USER"))),
					null,
					Collections.singletonList(new SimpleGrantedAuthority("USER")));
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}
		
		filterChain.doFilter(req, resp);
	}
}
