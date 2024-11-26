package br.com.forum.domain.curso;

public enum Categoria {
    BACKEND,
    FRONTEND,
    FULLSTACK;

    public static Categoria converteParaEnum(String categoria) {
        if (categoria == null || categoria.isBlank())
            throw new IllegalArgumentException("ENUM Categoria não pode ser nula ou vazia");

        var categoriaReescrita = categoria
                .toUpperCase()
                .replace(" ", "")
                .replace("-", "")
                .replace("_", "");
        return Categoria.valueOf(categoriaReescrita);
    }
}
