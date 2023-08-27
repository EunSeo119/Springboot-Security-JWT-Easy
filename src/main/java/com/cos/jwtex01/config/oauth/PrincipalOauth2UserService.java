package com.cos.jwtex01.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest: " + userReqeust.getClientRegistration());  // registrationId로 어떤 OAuth로 로그인 했는지 확인가능
        System.out.println("getAccessToken: " + userReqeust.getAccessToken().getTokenValue());
        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(Oauth-client라이브러리) -> AccessToken 요청 (여기까지가 userRequest 정보)
        // userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받아준다.
        System.out.println("getAttributes: " + super.loadUser(userRequest).getAttributes());    // 사실 이 정보만 있으면 됨!!(포함되어 있는 정보들: sub(pk), name(이름), given_name(이름), family_name(성), picture(프로필 사진), email(이메일), email_verified(만료되었나?), locale(한국))
        
        OAuth2User oauth2User = super.loadUser(userRequest);
        // 회원가입을 강제로 진행해볼 예정
        return super.loadUser(userRequest);
    }
}