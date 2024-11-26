package br.com.forum.domain.resposta;

import br.com.forum.domain.entidade_abstrata.EntidadeAbstrata;
import br.com.forum.domain.topico.Topico;
import br.com.forum.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "respostas")
public class Resposta extends EntidadeAbstrata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mensagem;

    @ManyToOne
    @JoinColumn(name = "topico_id", nullable = false)
    private Topico topico;

    @ManyToOne
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    private Boolean solucao = false;
}
