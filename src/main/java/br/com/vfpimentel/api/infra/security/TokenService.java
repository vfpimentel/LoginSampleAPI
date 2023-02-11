package br.com.vfpimentel.api.infra.security;

import br.com.vfpimentel.api.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.segredo}")
    private String secret;
    public String gerarToken(Usuario usuario) {
        try {
            Algorithm hmac256 = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("VictorPimentel")
                    .withSubject(usuario.getLogin())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generateExpiresAt())
                    .sign(hmac256);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerar token jwt", exception);
        }
    }
    public String getSubject(String tokenJWT) {
        try {
            Algorithm hmac256 = Algorithm.HMAC256(secret);
            return JWT.require(hmac256)
                    .withIssuer("VictorPimentel")
                    .build()
                    .verify(tokenJWT).getSubject();
        } catch (JWTVerificationException ex) {
            throw new RuntimeException("Token JWT inválido ou vencido");
        }



    }

    private Instant generateExpiresAt() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
