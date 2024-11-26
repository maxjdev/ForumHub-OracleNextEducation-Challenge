package br.com.forum.controller;

import br.com.forum.domain.usuario.UsuarioRequest;
import br.com.forum.domain.usuario.UsuarioResponse;
import br.com.forum.domain.usuario.UsuarioService;
import br.com.forum.domain.usuario.UsuarioUpdate;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponse> cadastrarUsuario(@RequestBody @Valid UsuarioRequest usuarioRequest,
                                                            UriComponentsBuilder uriBuilder) {
        var usuarioSalvo = usuarioService.cadastrarUsuario(usuarioRequest);
        var uri = uriBuilder
                .path("/usuario/{id}")
                .buildAndExpand(usuarioSalvo.id())
                .toUri();
        return ResponseEntity.created(uri).body(usuarioSalvo);
    }

    @GetMapping("/listar")
    public ResponseEntity<Page<UsuarioResponse>> listarUsuarios(
            @PageableDefault(size = 10, page = 0) Pageable paginacao) {
        return ResponseEntity.ok(usuarioService.listarUsuarios(paginacao));
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<UsuarioResponse> buscarUsuario(@PathVariable("id") Long id) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorId(id));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable("id") Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<UsuarioResponse> atualizarUsuario(@PathVariable("id") Long id,
                                                            @RequestBody @Valid UsuarioUpdate usuarioUpdate) {
        return ResponseEntity.ok(usuarioService.atualizarUsuario(id, usuarioUpdate));
    }
}
