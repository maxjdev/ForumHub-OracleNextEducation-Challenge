package br.com.forum.controller;

import br.com.forum.domain.topico.TopicoRequest;
import br.com.forum.domain.topico.TopicoResponse;
import br.com.forum.domain.topico.TopicoService;
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
@RequestMapping("/topico")
@SecurityRequirement(name = ConfiguracoesDeSeguranca.SEGURANCA)
@Tag(name = "Controller de tópico", description = "Endpoints para operações CRUD de tópico")
public class TopicoController {
    private final TopicoService topicoService;

    public TopicoController(TopicoService topicoService) {
        this.topicoService = topicoService;
    }

    @Operation(
            summary = "Cadastrar um novo tópico",
            description = "Registra um novo tópico no sistema com base nos dados fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Tópico criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados")
            }
    )
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

    @Operation(
            summary = "Listar tópicos cadastrados",
            description = "Retorna uma lista paginada de tópicos registrados no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de tópicos retornada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos")
            }
    )
    @GetMapping("/listar")
    public ResponseEntity<Page<TopicoResponse>> listarTopicos(
            @PageableDefault(size = 10, page = 0, direction = Sort.Direction.ASC) Pageable paginacao) {
        return ResponseEntity.ok(topicoService.listarTopicos(paginacao));
    }

    @Operation(
            summary = "Buscar tópico por ID",
            description = "Busca informações detalhadas de um tópico pelo ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tópico encontrado"),
                    @ApiResponse(responseCode = "404", description = "Tópico não encontrado")
            }
    )
    @GetMapping("/buscar/{id}")
    public ResponseEntity<TopicoResponse> buscarTopico(@PathVariable("id") Long id) {
        return ResponseEntity.ok(topicoService.buscarTopicoPorId(id));
    }

    @Operation(
            summary = "Deletar tópico",
            description = "Remove um tópico existente do sistema com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Tópico deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Tópico não encontrado")
            }
    )
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarTopico(@PathVariable("id") Long id) {
        topicoService.deletarTopico(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualizar status de um tópico",
            description = "Atualiza o status de um tópico existente no sistema com base no ID e status fornecidos.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status do tópico atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro nos parâmetros enviados"),
                    @ApiResponse(responseCode = "404", description = "Tópico não encontrado")
            }
    )
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<TopicoResponse> atualizarStatusDoTopico(@PathVariable("id") Long id,
                                                                  @RequestParam("status") String status) {
        return ResponseEntity.ok(topicoService.atualizarStatusDoTopico(id, status));
    }
}
