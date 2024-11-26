package br.com.forum.domain.usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarUsuarioPorId(Long id) {
        var usuario = buscaUsuarioPorIdOuLancaExcecao(id);
        return new UsuarioResponse(usuario);
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarUsuarios(Pageable paginacao) {
        return usuarioRepository.findAll(paginacao)
                .map(UsuarioResponse::new);
    }

    @Transactional
    public void deletarUsuario(Long id) {
        var usuario = buscaUsuarioPorIdOuLancaExcecao(id);
        usuarioRepository.delete(usuario);
    }

    @Transactional
    public UsuarioResponse atualizarUsuario(Long id, UsuarioUpdate usuarioUpdate) {
        if (usuarioRepository.existsByEmail(usuarioUpdate.email()))
            throw new IllegalArgumentException("Email já cadastrado");

        var usuario = buscaUsuarioPorIdOuLancaExcecao(id);
        usuario.setEmail(usuarioUpdate.email());

        Optional.ofNullable(usuarioUpdate.nome())
                .ifPresent(usuario::setNome);
        Optional.ofNullable(usuarioUpdate.senha())
                .ifPresent(usuario::setSenha);

        return new UsuarioResponse(usuarioRepository.save(usuario));
    }

    private Usuario buscaUsuarioPorIdOuLancaExcecao(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }
}
