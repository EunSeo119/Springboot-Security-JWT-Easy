package com.cos.jwtex01.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

import com.cos.jwtex01.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{

    @Autowired
    private BCryptPasswordEncoder BCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;
    
    // 구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest: " + userReqeust.getClientRegistration());  // registrationId로 어떤 OAuth로 로그인 했는지 확인가능
        System.out.println("getAccessToken: " + userReqeust.getAccessToken().getTokenValue());
        
        OAuth2User oauth2User = super.loadUser(userRequest);
        // 구글로그인 버튼 클릭 -> 구글로그인창 -> 로그인을 완료 -> code를 리턴(Oauth-client라이브러리) -> AccessToken 요청 (여기까지가 userRequest 정보)
        // userRequest 정보 -> loadUser함수 호출 -> 구글로부터 회원프로필 받아준다.
        System.out.println("getAttributes: " + oauth2User.getAttributes());    // 사실 이 정보만 있으면 됨!!(포함되어 있는 정보들: sub(pk), name(이름), given_name(이름), family_name(성), picture(프로필 사진), email(이메일), email_verified(만료되었나?), locale(한국))
        
        // 회원가입을 강제로 진행해볼 예정
        String provider = userRequest.getClientRegistration().getRegistrationId();    // google
        String providerId = oauth2User.getAttribute("sub");
        String username = provider+"_"+providerId;  // google_109742856182916427686
        String password = bCryptPasswordEncoder.encode("겟인데어");
        // username을 넣은적도 없고 password를 넣어준 적도 없지만 그냥 만들어주는 것임!
        String email = oauth2User.getAttribute("email");
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);    // 이미 회원가입이 되어있나?

        if(userEntity == null){
            System.out.println("구글 로그인이 최초입니다.");
            userEntity = User.builder()
                .username(username)
                .password(password)
                .email(email)
                .role(role)
                .provider(provider)
                .providerId(providerId)
                .build();
            userRepository.save(userEntity);
        } else {
            System.out.println("구글 로그인을 이미 한적이 있습니다. 당신은 자동회원가입이 되어 있습니다.");
        }

        return new PrincipalDetails(userEntity, oauth2User.getAttributes());
    }
}