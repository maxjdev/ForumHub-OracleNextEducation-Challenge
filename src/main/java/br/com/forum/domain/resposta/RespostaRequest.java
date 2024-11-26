package br.com.forum.domain.resposta;

public record RespostaRequest(
        Long topicoId,
        Long autorId,
        String mensagem){
}
