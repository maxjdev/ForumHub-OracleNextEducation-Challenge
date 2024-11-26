package br.com.forum.controller;

import br.com.forum.domain.resposta.RespostaRequest;
import br.com.forum.domain.resposta.RespostaResponse;
import br.com.forum.domain.resposta.RespostaService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/resposta")
public class RespostaController {
    private final RespostaService respostaService;

    public RespostaController(RespostaService respostaService) {
        this.respostaService = respostaService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<RespostaResponse> cadastrarResposta(@RequestBody @Valid RespostaRequest respostaRequest,
                                                              UriComponentsBuilder uriBuilder) {
        var respostaSalva = respostaService.cadastrarResposta(respostaRequest);
        var uri = uriBuilder
                .path("/resposta/{id}")
                .buildAndExpand(respostaSalva.id())
                .toUri();

        return ResponseEntity.created(uri).body(respostaSalva);
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<RespostaResponse>> listarRespostas(
            @PageableDefault(size = 10, page = 0) Pageable paginacao) {
        return ResponseEntity.ok(respostaService.listarRespostas(paginacao));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<RespostaResponse> buscarRespostaPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(respostaService.buscarRespostaPorId(id));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarResposta(@PathVariable("id") Long id) {
        respostaService.deletarResposta(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<RespostaResponse> atualizarSolucaoDaResposta(@PathVariable("id") Long id) {
        return ResponseEntity.ok(respostaService.atualizarSolucaoDaResposta(id));
    }
}
