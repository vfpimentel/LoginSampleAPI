package br.com.vfpimentel.api.domain.cadastro;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "cadastros")
@Entity(name = "Cadastro")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Cadastro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private Boolean ativo;

    public Cadastro(CadastroTO cadastroTO) {
        this.ativo = true;
        this.nome = cadastroTO.nome();
        this.email = cadastroTO.email();
        this.telefone = cadastroTO.telefone();
    }

    public void remove() {
        this.ativo = false;
    }

    public void update(CadastroTO cadastroUpdateTO) {
        if (cadastroUpdateTO.nome() != null) {
            this.nome = cadastroUpdateTO.nome();
        }
        if ( cadastroUpdateTO.telefone() != null) {
            this.telefone = cadastroUpdateTO.telefone();
        }
        if (cadastroUpdateTO.email() != null) {
            this.email = cadastroUpdateTO.email();
        }
    }

}
