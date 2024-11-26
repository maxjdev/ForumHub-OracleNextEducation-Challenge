package br.com.forum.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRequest(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha) {
    public Usuario toModel(UsuarioRequest usuarioRequest) {
        return Usuario.builder()
                .nome(usuarioRequest.nome)
                .email(usuarioRequest.email)
                .senha(usuarioRequest.senha)
                .build();
    }
}
