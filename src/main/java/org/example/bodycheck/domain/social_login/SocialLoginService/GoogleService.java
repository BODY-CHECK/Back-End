package org.example.bodycheck.domain.social_login.SocialLoginService;

import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bodycheck.domain.social_login.SocialLoginDTO.GoogleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class GoogleService {

    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private final String GAUTH_TOKEN_URL_HOST ;
    private final String GAUTH_USER_URL_HOST;

    @Autowired
    public GoogleService(@Value("${spring.google.client_id}") String clientId,
                         @Value("${spring.google.client_secret}") String clientSecret,
                         @Value("${spring.google.redirect_uri}") String redirectUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        GAUTH_TOKEN_URL_HOST = "https://oauth2.googleapis.com";
        GAUTH_USER_URL_HOST = "https://www.googleapis.com";
    }

    public String getAccessTokenFromGoogle(String code) {

        GoogleDTO.GoogleTokenResponseDTO googleTokenResponseDto = WebClient.create(GAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .path("/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("client_secret", clientSecret) // 환경변수로 관리
                        .queryParam("redirect_uri", redirectUri) // 환경변수로 관리
                        .queryParam("code", code)
                        .build())
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(GoogleDTO.GoogleTokenResponseDTO.class)
                .block();


        log.info(" [Google Service] Access Token ------> {}", googleTokenResponseDto.getAccessToken());
        log.info(" [Google Service] Refresh Token ------> {}", googleTokenResponseDto.getRefreshToken());
        log.info(" [Google Service] Scope ------> {}", googleTokenResponseDto.getScope());

        return googleTokenResponseDto.getAccessToken();
    }

    public GoogleDTO.GoogleUserInfoResponseDTO getUserInfo(String accessToken) {

        GoogleDTO.GoogleUserInfoResponseDTO userInfo = WebClient.create(GAUTH_USER_URL_HOST).get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth2/v2/userinfo")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(GoogleDTO.GoogleUserInfoResponseDTO.class)
                .block();

        log.info("[ Google Service ] User ID ---> {} ", userInfo.getId());
        log.info("[ Google Service ] Email ---> {} ", userInfo.getEmail());

        return userInfo;
    }
}
