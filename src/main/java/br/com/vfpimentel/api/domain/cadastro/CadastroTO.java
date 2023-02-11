package br.com.vfpimentel.api.domain.cadastro;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CadastroTO(
        Long id,
        @NotBlank
        String nome,
        @NotBlank @Email
        String email,
        String telefone) {

    public CadastroTO(Cadastro cadastro) {
        this(cadastro.getId(),
                cadastro.getNome(),
                cadastro.getEmail(),
                cadastro.getTelefone());
    }
}
