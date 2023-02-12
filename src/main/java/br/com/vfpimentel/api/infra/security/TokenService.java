package br.com.vfpimentel.api.infra.security;

import br.com.vfpimentel.api.domain.usuario.IUsuarioRepository;
import br.com.vfpimentel.api.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Autowired
    private IUsuarioRepository usuarioRepository;


    @Value("${api.security.token.segredo}")
    private String secret;
    public String gerarToken(Usuario usuario) {
        try {
            Algorithm hmac256 = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("VictorPimentel")
                    .withSubject(usuario.getLogin())
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

    @Transactional
    public Usuario updateLastAccess(Usuario usuario) {
        var usuarioBean = usuarioRepository.findById(usuario.getId()).get();
        usuarioBean.setLastAccess(LocalDateTime.now());
        return usuarioBean;
    }

    @Transactional
    public Usuario updateToken(Usuario usuario,String token) {
        usuario.setToken(token);
        return usuario;
    }

    private Instant generateExpiresAt() {
        return LocalDateTime.now().plusYears(2).toInstant(ZoneOffset.of("-03:00"));
    }


    public String getToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}
