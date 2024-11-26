package br.com.forum.domain.topico;

import br.com.forum.domain.curso.CursoRepository;
import br.com.forum.domain.usuario.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TopicoService {
    private final TopicoRepository topicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;

    public TopicoService(TopicoRepository topicoRepository, UsuarioRepository usuarioRepository, CursoRepository cursoRepository) {
        this.topicoRepository = topicoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cursoRepository = cursoRepository;
    }

    @Transactional
    public TopicoResponse cadastrarTopico(TopicoRequest topicoRequest) {
        if (topicoRepository.existsByTitulo(topicoRequest.titulo()))
            throw new IllegalArgumentException("Tópico já existente");

        var usuario = usuarioRepository.findById(topicoRequest.autorId())
                .orElseThrow(() -> new EntityNotFoundException("Autor não encontrado"));
        var curso = cursoRepository.findById(topicoRequest.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));

        var topico = Topico.builder()
                .titulo(topicoRequest.titulo())
                .mensagem(topicoRequest.mensagem())
                .autor(usuario)
                .curso(curso)
                .build();

        return new TopicoResponse(topicoRepository.save(topico));
    }

    @Transactional(readOnly = true)
    public TopicoResponse buscarTopicoPorId(Long id) {
        var topico = buscaTopicoPorIdOuLancaExcecao(id);
        return new TopicoResponse(topico);
    }

    @Transactional(readOnly = true)
    public Page<TopicoResponse> listarTopicos(Pageable paginacao) {
        return topicoRepository.findAll(paginacao)
                .map(TopicoResponse::new);
    }

    @Transactional
    public void deletarTopico(Long id) {
        var topico = buscaTopicoPorIdOuLancaExcecao(id);
        topicoRepository.delete(topico);
    }

    @Transactional
    public TopicoResponse atualizarStatusDoTopico(Long id, String status) {
        var topico = buscaTopicoPorIdOuLancaExcecao(id);
        topico.setStatus(Status.converteParaEnum(status));
        var topicoSalvo = topicoRepository.save(topico);
        return new TopicoResponse(topicoSalvo);
    }

    private Topico buscaTopicoPorIdOuLancaExcecao(Long id) {
        return topicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));
    }
}
