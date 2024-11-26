package br.com.forum.controller;

import br.com.forum.domain.resposta.RespostaRequest;
import br.com.forum.domain.resposta.RespostaResponse;
import br.com.forum.domain.resposta.RespostaService;
import br.com.forum.infra.security.ConfiguracoesDeSeguranca;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/resposta")
@SecurityRequirement(name = ConfiguracoesDeSeguranca.SEGURANCA)
@Tag(name = "Controller de Respostas", description = "Endpoints para gerenciamento de respostas")
public class RespostaController {
    private final RespostaService respostaService;

    public RespostaController(RespostaService respostaService) {
        this.respostaService = respostaService;
    }

    @Operation(
            summary = "Cadastrar nova resposta",
            description = "Adiciona uma nova resposta ao tópico especificado.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Resposta cadastrada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Tópico ou autor não encontrado"),
                    @ApiResponse(responseCode = "400", description = "Erro de validação nos campos da requisição")
            }
    )
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

    @Operation(
            summary = "Listar respostas cadastradas",
            description = "Retorna uma lista paginada de respostas cadastradas no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de respostas retornada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos")
            }
    )
    @GetMapping("/listar")
    public ResponseEntity<Page<RespostaResponse>> listarRespostas(
            @PageableDefault(size = 10, page = 0) Pageable paginacao) {
        return ResponseEntity.ok(respostaService.listarRespostas(paginacao));
    }

    @Operation(
            summary = "Buscar resposta por ID",
            description = "Busca informações detalhadas de uma resposta pelo ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resposta encontrada"),
                    @ApiResponse(responseCode = "404", description = "Resposta não encontrada")
            }
    )
    @GetMapping("/buscar/{id}")
    public ResponseEntity<RespostaResponse> buscarRespostaPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(respostaService.buscarRespostaPorId(id));
    }

    @Operation(
            summary = "Deletar resposta",
            description = "Remove uma resposta existente do sistema com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Resposta deletada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Resposta não encontrada")
            }
    )
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarResposta(@PathVariable("id") Long id) {
        respostaService.deletarResposta(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualizar status de solução de uma resposta",
            description = "Marca uma resposta como solução para o tópico.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resposta atualizada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Resposta não encontrada")
            }
    )
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<RespostaResponse> atualizarSolucaoDaResposta(@PathVariable("id") Long id) {
        return ResponseEntity.ok(respostaService.atualizarSolucaoDaResposta(id));
    }
}
