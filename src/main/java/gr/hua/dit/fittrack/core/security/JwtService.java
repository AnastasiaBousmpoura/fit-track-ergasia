package gr.hua.dit.fittrack.core.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final Key key;
    private final String issuer;
    private final String audience;
    private final long ttlMinutes;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.audience}") String audience,
            @Value("${app.jwt.ttl-minutes}") long ttlMinutes
    ) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.issuer = issuer;
        this.audience = audience;
        this.ttlMinutes = ttlMinutes;
    }

    /** Issue token for multiple roles */
    public String issue(String subject, Collection<String> roles) {

        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(subject)
                .setIssuer(issuer)
                .setAudience(audience)
                .claim("roles", roles)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(Duration.ofMinutes(ttlMinutes))))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /** Convenience for one role */
    public String issue(String subject, String role) {
        return issue(subject, List.of(role));
    }

    /** Parse + validate token */
    public Claims parse(String token) {
        return Jwts.parserBuilder()
                .requireIssuer(issuer)
                .requireAudience(audience)
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
