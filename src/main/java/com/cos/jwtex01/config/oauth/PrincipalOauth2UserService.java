package com.cos.jwtex01.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest: " + userReqeust.getClientRegistration());
        System.out.println("getAccessToken: " + userReqeust.getAccessToken().getTokenValue());
        System.out.println("getAttributes: " + super.loadUser(userRequest).getAttributes());    // 사실 이 정보만 있으면 됨!!(포함되어 있는 정보들: sub(pk), name(이름), given_name(이름), family_name(성), picture(프로필 사진), email(이메일), email_verified(만료되었나?), locale(한국))
        
        // 회원가입을 강제로 진행해볼 예정
        return super.loadUser(userRequest);
    }
}