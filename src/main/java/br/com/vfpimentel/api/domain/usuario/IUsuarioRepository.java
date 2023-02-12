package br.com.vfpimentel.api.domain.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByLogin(String login);
    Usuario findUserByLogin(String login);
    Usuario findUserByToken(String login);
}
