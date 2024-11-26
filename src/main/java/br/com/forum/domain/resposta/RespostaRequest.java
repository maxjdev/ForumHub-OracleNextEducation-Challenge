package br.com.forum.domain.resposta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RespostaRequest(
        @NotNull Long topicoId,
        @NotNull Long autorId,
        @NotBlank String mensagem){
}
