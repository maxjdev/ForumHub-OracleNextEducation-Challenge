package br.com.forum.domain.topico;

public enum Status {
    ABERTO,
    FECHADO,
    RESOLVIDO;

    public static Status converteParaEnum(String status) {
        if (status == null || status.isBlank())
            throw new IllegalArgumentException("ENUM Status não pode ser nula ou vazia");

        var statusReescrito = status
                .toUpperCase()
                .replace(" ", "")
                .replace("-", "")
                .replace("_", "");
        return Status.valueOf(statusReescrito);
    }
}
