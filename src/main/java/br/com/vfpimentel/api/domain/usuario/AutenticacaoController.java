package br.com.vfpimentel.api.domain.usuario;

import br.com.vfpimentel.api.infra.security.JWTTokenTO;
import br.com.vfpimentel.api.infra.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    @Transactional
    public ResponseEntity login(@RequestBody @Valid UsuarioTO data) {
        if (data.login() == null || data.senha() ==null) {
            return ResponseEntity.badRequest().build();
        }
        //localiza o usuário pelo login
        var usuario = usuarioRepository.findUserByLogin(data.login());
        if (usuario == null) {
            return ResponseEntity.badRequest().build();
        }
        var authenticationToken = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
        //autentica o usuario no spring boot
        var authentication = authenticationManager.authenticate(authenticationToken);
        // gera e atribui o token ao usuário
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        usuario.setToken(tokenJWT);
        usuario.setLastAccess(LocalDateTime.now());
        return ResponseEntity.ok(new JWTTokenTO(usuario.getLogin(),tokenJWT));
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity logout(HttpServletRequest request) {
        var tokenJWT = tokenService.getToken(request);
        if (tokenJWT == null) {
            return ResponseEntity.badRequest().build();
        }
        //localiza o usuário pelo login
        var usuario = usuarioRepository.findUserByToken(tokenJWT);
        if (usuario == null) {
            return ResponseEntity.ok("Usuario não encontrado.");
        }
        usuario.setLastAccess(null);
        usuario.setToken(null);
        return ResponseEntity.ok("Logout realizado.");
    }
}
