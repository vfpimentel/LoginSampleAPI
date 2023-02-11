package br.com.vfpimentel.api.domain.cadastro;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICadastroRepository extends JpaRepository<Cadastro, Long> {
    Page<Cadastro> findAllByAtivoTrue(Pageable paginacao);
}
