package br.com.vfpimentel.api.domain.cadastro;

import br.com.vfpimentel.api.domain.usuario.IUsuarioRepository;
import br.com.vfpimentel.api.infra.security.TokenService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cadastros")
public class CadastroController {

    @Autowired
    private ICadastroRepository cadastroRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    @Transactional
    public ResponseEntity criar(@RequestBody @Valid CadastroTO body, UriComponentsBuilder uriBuilder) {
        try {
            var cadastro = new Cadastro(body);
            cadastroRepository.save(cadastro);
            var uri = uriBuilder.path("cadastro/{id}").buildAndExpand(cadastro.getId()).toUri();
            return ResponseEntity.created(uri).body(new CadastroTO(cadastro));
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity consultar(@PathVariable Long id) {
        var cadastro = cadastroRepository.getReferenceById(id);
        return ResponseEntity.ok(new CadastroTO(cadastro));
    }

    @GetMapping
    public ResponseEntity<Page<CadastroTO>> listar(@PageableDefault(size = 10, page = 0, sort = {"nome"}) Pageable paginacao) {
        var page = cadastroRepository.findAllByAtivoTrue(paginacao).map(CadastroTO::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid CadastroTO body) {
        var cadastro = cadastroRepository.getReferenceById(body.id());
        cadastro.update(body);
        //a tag transactional gera um commit e o hibenate identifica que o objeto foi alterado.
        return ResponseEntity.ok(new CadastroTO(cadastro));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        var cadastro = cadastroRepository.getReferenceById(id);
        cadastro.remove();
        return ResponseEntity.noContent().build();
    }
}
