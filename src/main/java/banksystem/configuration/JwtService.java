package banksystem.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {
    @Value("${app.jwt.secret}") String SECRET_KEY;
    public String generateToken(String username, List<String> roles) {
    return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .addClaims(Map.of("roles", roles))
            .signWith(getSignedKey(), SignatureAlgorithm.HS256)
            .compact();
    }
    private Key getSignedKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    public Claims verifyTokenAndExtractClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignedKey())
                .build()
                .parseSignedClaims(token).getPayload();

    }
    public String extractUsername(String token) {
        return verifyTokenAndExtractClaims(token).getSubject();
    }
    public List<String> extractRoles(String token) {
        return (List<String>) verifyTokenAndExtractClaims(token).get("roles");
    }
}
