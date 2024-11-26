package br.com.forum.domain.topico;

import br.com.forum.domain.curso.Curso;
import br.com.forum.domain.entidade_abstrata.EntidadeAbstrata;
import br.com.forum.domain.resposta.Resposta;
import br.com.forum.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "topicos")
public class Topico extends EntidadeAbstrata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String mensagem;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario autor;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @OneToMany(mappedBy = "topico", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Resposta> respostas = new ArrayList<>();

    @PrePersist
    protected void inCreation() {
        this.status = Status.ABERTO;
    }
}
