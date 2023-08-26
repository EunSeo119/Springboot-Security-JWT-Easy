package com.cos.jwtex01.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cos.jwtex01.config.auth.PrincipalDetails;
import com.cos.jwtex01.model.User;
import com.cos.jwtex01.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
// @CrossOrigin  // CORS 허용 
public class RestApiController {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;	// 어디선가 @Bean 으로 IoC에 등록해줘서 주입함.
	
	// 모든 사람이 접근 가능
	@GetMapping("home")
	public String home() {
		return "<h1>home</h1>";
	}
	
	// Tip : JWT를 사용하면 UserDetailsService를 호출하지 않기 때문에 @AuthenticationPrincipal 사용 불가능.
	// 왜냐하면 @AuthenticationPrincipal은 UserDetailsService에서 리턴될 때 만들어지기 때문이다.
	
	// 유저 혹은 매니저 혹은 어드민이 접근 가능
	@GetMapping("user")
	public String user(Authentication authentication) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("principal : "+principal.getUser().getId());
		System.out.println("principal : "+principal.getUser().getUsername());
		System.out.println("principal : "+principal.getUser().getPassword());
		
		return "<h1>user</h1>";
	}
	
	// 매니저 혹은 어드민이 접근 가능
	@GetMapping("manager/reports")
	public String reports() {
		return "<h1>reports</h1>";
	}
	
	// 어드민이 접근 가능
	@GetMapping("admin/users")
	public List<User> users(){
		return userRepository.findAll();
	}
	
	@PostMapping("join")
	public String join(@RequestBody User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));	// 암호화 해줘야 시큐리티로 로그인 가능함!
		user.setRoles("ROLE_USER");
		userRepository.save(user);	// 이것만 있으면 회원가입은 됨. 하지만 시큐리티로 로그인 할 수 없음. 이유는 패스워드가 암호화가 안되었기 때문!
		return "회원가입완료";
	}

	// ======== 강의 들으면서 추가사항 ==========

	// 얘네는 글로벌하게 말고 메서드 하나하나에 걸고 싶을 때 씀!

	@Secured("ROLE_ADMIN")	// 하나만 걸고 싶을 때!
	@GetMapping("/info")
	public @ResponseBody String info() {
		// @EnableGlobalMethodSecurity(securedEnabled=true) 이걸 해주므로써 시큐리티가 적용된당!(로그인 해줘야 접근 가능해진다)
		return "개인정보";
	}

	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")	// 여러개 걸고 싶을때!, data라는 메서드가 실행되기 직전에 실행됨!
	@GetMapping("/data")
	public @ResponseBody String data() {
		// 위 두개의 권한이 있을 때 들어와 진다!(manager, admin)
		return "데이터정보";
	}
	
}











