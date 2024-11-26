package br.com.forum.domain.topico;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record TopicoResponse(
        Long id,
        String titulo,
        String mensagem,
        Status status,
        @JsonFormat
        Instant dataCriacao,
        Long autorId,
        Long cursoId){
        public TopicoResponse(Topico topico) {
            this(
                    topico.getId(),
                    topico.getTitulo(),
                    topico.getMensagem(),
                    topico.getStatus(),
                    topico.getDataCriacao(),
                    topico.getAutor().getId(),
                    topico.getCurso().getId());
        }
}
