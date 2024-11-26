package br.com.forum.domain.entidade_abstrata;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@MappedSuperclass
public abstract class EntidadeAbstrata {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @Column(updatable = false, nullable = false)
    protected Instant dataCriacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = Instant.now();
    }
}
