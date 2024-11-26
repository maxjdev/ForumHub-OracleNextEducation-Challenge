package br.com.forum.domain.resposta;

public record RespostaResponse(
        Long id,
        String mensagem,
        Long autorId,
        Long topicoId,
        Boolean solucao){
       public RespostaResponse(Resposta resposta) {
            this(
                    resposta.getId(),
                    resposta.getMensagem(),
                    resposta.getAutor().getId(),
                    resposta.getTopico().getId(),
                    resposta.getSolucao());
       }
}
