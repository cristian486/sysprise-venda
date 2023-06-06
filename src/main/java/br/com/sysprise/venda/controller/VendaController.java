package br.com.sysprise.venda.controller;


import br.com.sysprise.venda.model.*;
import br.com.sysprise.venda.service.VendaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@CrossOrigin
@RestController
@RequestMapping("/venda")
@AllArgsConstructor
public class VendaController {

    private final VendaService vendaService;

    @GetMapping
    public ResponseEntity<Page<DadosListagemVenda>> listar(@PageableDefault(sort = "id", size = 5) Pageable pageable) {
        Page<DadosListagemVenda> dadosListagem = vendaService.listar(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(dadosListagem);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoVenda> detlahar(@PathVariable("id") Long id) {
        DadosDetalhamentoVenda dadosDetalhamento = vendaService.detalhar(id);
        return ResponseEntity.status(HttpStatus.OK).body(dadosDetalhamento);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoVenda> cadastrar(@RequestBody @Valid DadosCadastroVenda dadosCadastro, UriComponentsBuilder componentsBuilder) {
        DadosDetalhamentoVenda dadosDetalhamento = vendaService.cadastrar(dadosCadastro);
        URI uri = componentsBuilder.path("/venda/{id}").buildAndExpand(dadosDetalhamento.id()).toUri();
        return ResponseEntity.created(uri).body(dadosDetalhamento);
    }

    @PatchMapping
    @Transactional
    public ResponseEntity<?> atualizarStatusVenda(@RequestBody @Valid DadosAtualizarStatusVenda dadosAtualizar) {
        vendaService.atualizarStatus(dadosAtualizar);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping
    @Transactional
    public ResponseEntity<DadosDetalhamentoVenda> atualizar(@RequestBody @Valid DadosAtualizarVenda dadosAtualizar) {
        DadosDetalhamentoVenda dadosDetalhamento = vendaService.atualizar(dadosAtualizar);
        return ResponseEntity.status(HttpStatus.OK).body(dadosDetalhamento);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable("id") Long id) {
        vendaService.deletar(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
