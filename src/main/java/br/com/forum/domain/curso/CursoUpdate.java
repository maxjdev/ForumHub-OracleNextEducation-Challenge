package br.com.forum.domain.curso;

import jakarta.validation.constraints.NotBlank;

public record CursoUpdate(@NotBlank String nome) {
}
