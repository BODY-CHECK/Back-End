package org.example.bodycheck.domain.social_login.SocialLoginDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GoogleDTO {

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoogleTokenResponseDTO {
        @JsonProperty("token_type")
        public String tokenType;
        @JsonProperty("access_token")
        public String accessToken;
        @JsonProperty("id_token")
        public String idToken;
        @JsonProperty("expires_in")
        public Integer expiresIn;
        @JsonProperty("refresh_token")
        public String refreshToken;
        @JsonProperty("refresh_token_expires_in")
        public Integer refreshTokenExpiresIn;
        @JsonProperty("scope")
        public String scope;
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GoogleUserInfoResponseDTO {
        @JsonProperty("id")
        public String id;

        @JsonProperty("email")
        public String email;

        @JsonProperty("nickname")
        public String nickName;
    }
}
