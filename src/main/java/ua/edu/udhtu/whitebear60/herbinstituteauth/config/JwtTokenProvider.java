package ua.edu.udhtu.whitebear60.herbinstituteauth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.edu.udhtu.whitebear60.herbinstituteauth.model.AccountEntity;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.validity}")
    private long validity;

    public String createToken(AccountEntity account) {
        Claims claims = Jwts.claims().subject(account.getLogin()).add("roles", buildRoles(account)).build();
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.validity);

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA384");
        Logger.getAnonymousLogger().info(key.getAlgorithm());

        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key, Jwts.SIG.HS384)
                .compact();

    }

    private List<String> buildRoles(AccountEntity accountEntity) {
        List<String> roles = new ArrayList<>();

        if (accountEntity != null && accountEntity.getRoles() != null) {
            accountEntity.getRoles().forEach(roleEntity ->
                    roles.add(roleEntity.getRole()));
        }

        return roles;
    }
}
