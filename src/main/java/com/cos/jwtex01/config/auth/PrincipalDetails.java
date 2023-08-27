package com.cos.jwtex01.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.jwtex01.model.User;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
// 로그인을 진행이 완료가 되면 시큐리티 session을 만들어줍니다.(Security ContextHolder)
// 오브젝트 타입 => Authentication 타입 객체
// Authentication 안에 User정보가 있어야 됨.
// User오브젝트 타입 => UserDetails 타입 객체

// Security Session => Authentication(여기에 들어갈 수 있는 객체는 정해져있음) => UserDetails(이 타입으로 저장함)(=PrincipalDetails => PrincipalDetails를 Authentication 객체 안에 넣을 수 있다!)
// => 그래서 꺼내 쓸 때 Security Session을 꺼내면 Authentication 위치가 나오고 이 안에서 UserDetails 객체를 꺼내면 user object에 접근할 수 있음!
// PrincipalDetails 를 Authentication에 넣을 수 있고, Authentication 객체를 만들어서 Session에 넣음

@Data
public class PrincipalDetails implements UserDetails, OAuth2User{

	private User user;  // 콤포지션

    public PrincipalDetails(User user){
        this.user = user;
    }

    public User getUser() {
		return user;
	}

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        // 계정 만료된 이후로 안이용했으면 true
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // 니 계정 잠겼니? -> 아니요
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 계정 비밀번호 너무 오래돼서 만료되었나? -> 아니요
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정 활성화 되어있니 -> 아니요
        // 우리 사이트! 1년동안 회원이 로그인을 안하면! 휴먼 계정으로 하기로 함. -> 마지막 로그인 날짜 가져와서 -> 현재시간 - 로긴시간 => 1년 초과하면 return false;
        return true;
    }
    
    // 해당 User의 권한을 리턴하는 곳!
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        user.getRoleList().forEach(r -> {
        	authorities.add(()->{ return r;});
        });
        return authorities;
    }
}
