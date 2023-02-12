package br.com.vfpimentel.api.infra.security;

import br.com.vfpimentel.api.domain.usuario.IUsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Value("${api.security.token.timeout_session}")
    private int TIMEOUT_SESSION;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = tokenService.getToken(request);
        if (tokenJWT != null) {
            //localizar o usuario pelo token
            var usuario = usuarioRepository.findUserByToken(tokenJWT);
            if (usuario == null) {
                filterChain.doFilter(request,response);
                return;
            }
            //verifica o lastAccess em relação ao timeout de sessão
            var diffInSeconds = usuario.getLastAccess() != null ? ChronoUnit.SECONDS.between(usuario.getLastAccess(), LocalDateTime.now()) : 0;
            if (usuario.getLastAccess() != null && diffInSeconds <= TIMEOUT_SESSION ) {
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                //atualiza o lastAccess
                tokenService.updateLastAccess(usuario);
            }
        }
        filterChain.doFilter(request,response);
    }

}
