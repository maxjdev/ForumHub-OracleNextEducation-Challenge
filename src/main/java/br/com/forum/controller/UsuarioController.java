package br.com.forum.controller;

import br.com.forum.domain.usuario.UsuarioResponse;
import br.com.forum.domain.usuario.UsuarioService;
import br.com.forum.domain.usuario.UsuarioUpdate;
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

@RestController
@RequestMapping("/usuario")
@SecurityRequirement(name = ConfiguracoesDeSeguranca.SEGURANCA)
@Tag(name = "Controller de usuário", description = "Endpoints para operações de usuário")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Listar usuários cadastrados",
            description = "Retorna uma lista paginada de usuários cadastrados no sistema.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Parâmetros de paginação inválidos")
            }
    )
    @GetMapping("/listar")
    public ResponseEntity<Page<UsuarioResponse>> listarUsuarios(
            @PageableDefault(size = 10, page = 0) Pageable paginacao) {
        return ResponseEntity.ok(usuarioService.listarUsuarios(paginacao));
    }

    @Operation(
            summary = "Buscar usuário por ID",
            description = "Busca informações detalhadas de um usuário pelo ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    @GetMapping("/buscar/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuario(@PathVariable("id") Long id) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorId(id));
    }

    @Operation(
            summary = "Deletar usuário por id",
            description = "Remove um usuário existente do sistema com base no ID fornecido.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable("id") Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualizar informações de um usuário",
            description = "Atualiza os dados de um usuário existente no sistema. Pode alterar o nome, email ou senha.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro de validação ou email já cadastrado"),
                    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
            }
    )
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(@PathVariable("id") Long id,
                                                            @RequestBody @Valid UsuarioUpdate usuarioUpdate) {
        return ResponseEntity.ok(usuarioService.atualizarUsuario(id, usuarioUpdate));
    }
}
