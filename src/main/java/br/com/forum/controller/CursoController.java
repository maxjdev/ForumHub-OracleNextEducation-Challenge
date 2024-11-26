package br.com.forum.controller;

import br.com.forum.domain.curso.CursoRequest;
import br.com.forum.domain.curso.CursoResponse;
import br.com.forum.domain.curso.CursoService;
import br.com.forum.domain.curso.CursoUpdate;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/curso")
public class CursoController {
    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<CursoResponse> cadastrarCurso(@RequestBody @Valid CursoRequest cursoRequest,
                                                        UriComponentsBuilder uriBuilder) {
        var cursoSalvo = cursoService.cadastrarCurso(cursoRequest);
        var uri = uriBuilder
                .path("/curso/{id}")
                .buildAndExpand(cursoSalvo.id())
                .toUri();

        return ResponseEntity.created(uri).body(cursoSalvo);
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<CursoResponse>> listarCursos(
            @PageableDefault(size = 10, page = 0, sort = {"nome"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        return ResponseEntity.ok(cursoService.listarCursos(paginacao));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<CursoResponse> buscarCursoPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cursoService.buscarCursoPorId(id));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarCurso(@PathVariable("id") Long id){
        cursoService.deletarCurso(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<CursoResponse> atualizarCurso(@PathVariable("id") Long id,
                                                        @RequestBody @Valid CursoUpdate cursoUpdate){
        return ResponseEntity.ok(cursoService.atualizarCurso(id, cursoUpdate));
    }
}
