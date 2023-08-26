package com.cos.jwtex01.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.jwtex01.model.User;
import com.cos.jwtex01.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// 시큐리티 설정에서 loginProcessingUrl("/login"); -> 지금 버전에서 해당사항 없긴함
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행 -> 이건 걍 규칙임!
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	
	// 시큐리티 session(내부 Authentication(내부 UserDetails))
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService : 진입");
		User user = userRepository.findByUsername(username);

		// session.setAttribute("loginUser", user);
		return new PrincipalDetails(user);
	}
}
