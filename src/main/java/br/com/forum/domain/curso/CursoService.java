package br.com.forum.domain.curso;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CursoService {
    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @Transactional(readOnly = true)
    public Page<CursoResponse> listarCursos(Pageable paginacao) {
        return cursoRepository.findAll(paginacao)
                .map(CursoResponse::new);
    }

    @Transactional(readOnly = true)
    public CursoResponse buscarCursoPorId(Long id) {
        var curso = buscaCursoPorIdOuLancaExcecao(id);
        return new CursoResponse(curso);
    }

    @Transactional
    public CursoResponse cadastrarCurso(CursoRequest cursoRequest) {
        if (cursoRepository.existsByNome(cursoRequest.nome())) 
            throw new IllegalArgumentException("Curso já cadastrado");
            
        var curso = cursoRequest.toModel(cursoRequest);
        var cursoSalvo = cursoRepository.save(curso);
        return new CursoResponse(cursoSalvo);
    }

    @Transactional
    public void deletarCurso(Long id) {
        var curso = buscaCursoPorIdOuLancaExcecao(id);
        cursoRepository.delete(curso);
    }

    @Transactional
    public CursoResponse atualizarCurso(Long id, CursoUpdate cursoUpdate) {
        var curso = buscaCursoPorIdOuLancaExcecao(id);
        curso.setNome(cursoUpdate.nome());
        cursoRepository.save(curso);
        return new CursoResponse(curso);
    }

    private Curso buscaCursoPorIdOuLancaExcecao(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Curso não encontrado"));
    }
}
