package br.com.forum.domain.resposta;

import br.com.forum.domain.topico.TopicoRepository;
import br.com.forum.domain.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RespostaService {
    private final RespostaRepository respostaRepository;
    private final TopicoRepository topicoRepository;
    private final UsuarioRepository usuarioRepository;


    public RespostaService(RespostaRepository respostaRepository, TopicoRepository topicoRepository, UsuarioRepository usuarioRepository) {
        this.respostaRepository = respostaRepository;
        this.topicoRepository = topicoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public RespostaResponse buscarRespostaPorId(Long id) {
        var resposta = buscaRespostaPorIdOuLancaExcecao(id);
        return new RespostaResponse(resposta);
    }

    @Transactional(readOnly = true)
    public Page<RespostaResponse> listarRespostas(Pageable paginacao) {
        return respostaRepository.findAll(paginacao)
                .map(RespostaResponse::new);
    }

    @Transactional
    public void deletarResposta(Long id) {
        var resposta = buscaRespostaPorIdOuLancaExcecao(id);
        respostaRepository.delete(resposta);
    }

    @Transactional
    public RespostaResponse cadastrarResposta(RespostaRequest respostaRequest) {
        var topico = topicoRepository.findById(respostaRequest.topicoId())
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));
        var autor = usuarioRepository.findById(respostaRequest.autorId())
                .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado"));

        var resposta = Resposta.builder()
                .mensagem(respostaRequest.mensagem())
                .topico(topico)
                .autor(autor)
                .build();

        return new RespostaResponse(respostaRepository.save(resposta));
    }

    @Transactional
    public RespostaResponse atualizarSolucaoDaResposta(Long id) {
        var resposta = buscaRespostaPorIdOuLancaExcecao(id);
        resposta.setSolucao(true);
        var respostaSalva = respostaRepository.save(resposta);
        return new RespostaResponse(respostaSalva);
    }

    private Resposta buscaRespostaPorIdOuLancaExcecao(Long id) {
        return respostaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resposta não encontrada"));
    }
}
