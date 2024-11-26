package br.com.forum.domain.curso;

public record CursoResponse(
        Long id,
        String nome,
        String categoria){
    public CursoResponse(Curso curso) {
        this(curso.getId(), curso.getNome(), curso.getCategoria().toString());
    }
}
