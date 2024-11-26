package br.com.forum.controller;

import br.com.forum.domain.curso.CursoRequest;
import br.com.forum.domain.curso.CursoResponse;
import br.com.forum.domain.curso.CursoService;
import br.com.forum.domain.curso.CursoUpdate;
import br.com.forum.infra.security.ConfiguracoesDeSeguranca;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = ConfiguracoesDeSeguranca.SEGURANCA)
@Tag(name = "Controller de curso", description = "Endpoints CRUD para curso")
public class CursoController {
    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @Operation(
            summary = "Cadastrar um novo curso",
            description = "Endpoint para cadastrar um novo curso no sistema.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Curso cadastrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro de validação ou curso já existente")
            }
    )
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

    @Operation(
            summary = "Listar cursos cadastrados",
            description = "Retorna uma lista paginada de cursos com informações básicas.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de cursos retornada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos")
            }
    )
    @GetMapping("/listar")
    public ResponseEntity<Page<CursoResponse>> listarCursos(
            @PageableDefault(size = 10, page = 0, sort = {"nome"}, direction = Sort.Direction.ASC) Pageable paginacao) {
        return ResponseEntity.ok(cursoService.listarCursos(paginacao));
    }

    @Operation(
            summary = "Buscar curso por ID",
            description = "Busca informações detalhadas de um curso pelo ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Curso encontrado"),
                    @ApiResponse(responseCode = "404", description = "Curso não encontrado")
            }
    )
    @GetMapping("/buscar/{id}")
    public ResponseEntity<CursoResponse> buscarCursoPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cursoService.buscarCursoPorId(id));
    }

    @Operation(
            summary = "Deletar curso",
            description = "Remove um curso existente do sistema com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Curso deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Curso não encontrado")
            }
    )
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarCurso(@PathVariable("id") Long id){
        cursoService.deletarCurso(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualizar curso",
            description = "Atualiza as informações de um curso existente.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Curso atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro de validação"),
                    @ApiResponse(responseCode = "404", description = "Curso não encontrado")
            }
    )
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<CursoResponse> atualizarCurso(@PathVariable("id") Long id,
                                                        @RequestBody @Valid CursoUpdate cursoUpdate){
        return ResponseEntity.ok(cursoService.atualizarCurso(id, cursoUpdate));
    }
}
