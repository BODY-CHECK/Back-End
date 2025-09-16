package org.example.bodycheck.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtParser {

    public <T> T getClaim(String token, String claimKey, Class<T> type) {
        Object value = parseToken(token).get(claimKey);
        if (type.isInstance(value)) return type.cast(value);
        return null;
    }

    private Claims parseToken(String accessToken) {
        try {
            return Jwts.parserBuilder().build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
