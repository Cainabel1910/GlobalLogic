package com.globallogic.usuariobci.usuario_bci.auth;

import com.globallogic.usuariobci.usuario_bci.jwt.JwtService;
import com.globallogic.usuariobci.usuario_bci.model.Role;
import com.globallogic.usuariobci.usuario_bci.model.Usuario;
import com.globallogic.usuariobci.usuario_bci.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public UsuarioResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        //Verificamos usuario
        UserDetails user = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();
        //Generamos el token
        String token = jwtService.getToken(user);
        //Asignamos valor del nuevo token y actualizamos la fecha de loguin
        Usuario usuarioBD = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();
        usuarioBD.setLastLogin(new Date(System.currentTimeMillis()));
        usuarioBD.setToken(token);
        usuarioBD = usuarioRepository.save(usuarioBD);

        return new UsuarioResponse(
                usuarioBD.getId(),
                usuarioBD.getCreated(),
                usuarioBD.getLastLogin(),
                usuarioBD.getToken(),
                usuarioBD.isActive(),
                usuarioBD.getName(),
                usuarioBD.getEmail(),
                usuarioBD.getPassword(),
                usuarioBD.getPhones()
        );
    }

    public UsuarioResponse register(Usuario request) {
        //Asignamos el rol por defecto y codificamos la contraseña
        request.setRole(Role.USER);
        request.setPassword(passwordEncoder.encode( request.getPassword()));
        Usuario usuarioNew = usuarioRepository.save(request);
        //Asignamos el nuevo token
        usuarioNew.setToken(jwtService.getToken(request));

        return new UsuarioResponse(
                usuarioNew.getId(),
                usuarioNew.getCreated(),
                usuarioNew.getLastLogin(),
                usuarioNew.getToken(),
                usuarioNew.isActive(),
                usuarioNew.getName(),
                usuarioNew.getEmail(),
                usuarioNew.getPassword(),
                usuarioNew.getPhones()
        );

    }
}
