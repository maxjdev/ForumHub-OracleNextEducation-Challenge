package br.com.forum.controller;

import br.com.forum.domain.topico.TopicoRequest;
import br.com.forum.domain.topico.TopicoResponse;
import br.com.forum.domain.topico.TopicoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topico")
public class TopicoController {
    private final TopicoService topicoService;

    public TopicoController(TopicoService topicoService) {
        this.topicoService = topicoService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<TopicoResponse> cadastrarTopico(@RequestBody @Valid TopicoRequest topicoRequest,
                                                          UriComponentsBuilder uriBuilder) {
        var topicoSalvo = topicoService.cadastrarTopico(topicoRequest);
        var uri = uriBuilder
                .path("/topico/{id}")
                .buildAndExpand(topicoSalvo.id())
                .toUri();
        return ResponseEntity.created(uri).body(topicoSalvo);
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<TopicoResponse>> listarTopicos(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable paginacao) {
        return ResponseEntity.ok(topicoService.listarTopicos(paginacao));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<TopicoResponse> buscarTopico(@PathVariable("id") Long id) {
        return ResponseEntity.ok(topicoService.buscarTopicoPorId(id));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarTopico(@PathVariable("id") Long id) {
        topicoService.deletarTopico(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<TopicoResponse> atualizarStatusDoTopico(@PathVariable("id") Long id,
                                                                  @RequestParam("status") String status) {
        return ResponseEntity.ok(topicoService.atualizarStatusDoTopico(id, status));
    }
}
