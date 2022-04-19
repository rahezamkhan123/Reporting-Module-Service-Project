package com.alzohar.service.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alzohar.service.impl.JWTUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LogManager.getLogger(JwtRequestFilter.class);

	@Autowired
	private JWTUserDetailsService jwtUserDetailsService;

	@Autowired
	private JWTTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String username = null;
		String jwtToken = null;

		final String TOKEN = request.getHeader("Authorization");
		if (TOKEN != null && TOKEN.startsWith("Bearer ")) {
			jwtToken = TOKEN.substring(7);
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				LOGGER.error("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				LOGGER.error("JWT Token has expired");
			}
		}

		if (username != null) {
			UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(getUserPaaswordAuthToken(userDetails, request));
			}
		}
		filterChain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getUserPaaswordAuthToken(UserDetails userDetails,
			HttpServletRequest request) {
		UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userDetails, null,
				userDetails.getAuthorities());
		upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return upat;
	}

}