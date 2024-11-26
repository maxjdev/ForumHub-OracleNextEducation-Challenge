package br.com.forum.controller;

import br.com.forum.infra.security.AutenticacaoRequest;
import br.com.forum.domain.usuario.UsuarioRequest;
import br.com.forum.infra.security.AutenticacaoService;
import br.com.forum.infra.security.DadosTokenJWT;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/autenticacao")
@Tag(name = "Controller de autenticação", description = "Endpoints de login e cadastro de usuário")
public class AutenticacaoController {
    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @Operation(summary = "Endpoint de login",
            description = "Efetua login",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro ao efetuar login"),
                    @ApiResponse(responseCode = "404", description = "Usuario não encontrado")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid AutenticacaoRequest autenticacaoRequest) {
        return ResponseEntity.ok(autenticacaoService.efetuarLogin(autenticacaoRequest));
    }

    @Operation(summary = "Endpoint de cadastro",
            description = "Cadastra um novo usuario",
            responses =  {
                    @ApiResponse(responseCode = "201", description = "Usuario cadastrado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro ao cadastrar usuario")
            }
    )
    @PostMapping("/cadastrar")
    public ResponseEntity cadastrarUsuario(@RequestBody @Valid UsuarioRequest usuarioRequest,
                                           UriComponentsBuilder uriBuilder) {
        var usuarioSalvo = autenticacaoService.cadastrarUsuario(usuarioRequest);
        var uri = uriBuilder
                .path("/usuario/{id}")
                .buildAndExpand(usuarioSalvo.id())
                .toUri();
        return ResponseEntity.created(uri).body(usuarioSalvo);
    }
}
