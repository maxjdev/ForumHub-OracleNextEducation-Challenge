package br.com.forum.infra.security;

import br.com.forum.domain.usuario.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutenticacaoService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public AutenticacaoService(AuthenticationManager authenticationManager, TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }


    public DadosTokenJWT efetuarLogin(AutenticacaoRequest autenticacaoRequest) {
        var tokenAutenticacao = new UsernamePasswordAuthenticationToken(autenticacaoRequest.email(), autenticacaoRequest.senha());
        var autenticacao = authenticationManager.authenticate(tokenAutenticacao);
        var tokenJwt = tokenService.gerarToken((Usuario) autenticacao.getPrincipal());
        return new DadosTokenJWT(tokenJwt);
    }

    @Transactional
    public UsuarioResponse cadastrarUsuario(UsuarioRequest usuarioRequest) {
        if (usuarioRepository.existsByEmail(usuarioRequest.email()))
            throw new IllegalArgumentException("E-mail já cadastrado");

        var encriptedPassword = new BCryptPasswordEncoder().encode(usuarioRequest.senha());
        var novoUsuario = Usuario.builder()
                .nome(usuarioRequest.nome())
                .email(usuarioRequest.email())
                .senha(encriptedPassword)
                .build();
        var usuarioSalvo = usuarioRepository.save(novoUsuario);

        return new UsuarioResponse(usuarioSalvo);
    }
}
