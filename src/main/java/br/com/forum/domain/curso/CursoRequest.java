package br.com.forum.domain.curso;

import jakarta.validation.constraints.NotBlank;

public record CursoRequest(
        @NotBlank String nome,
        @NotBlank String categoria){
    public Curso toModel(CursoRequest cursoRequest) {
        return Curso.builder()
                .nome(cursoRequest.nome)
                .categoria(Categoria.converteParaEnum(cursoRequest.categoria))
                .build();
    }
}
