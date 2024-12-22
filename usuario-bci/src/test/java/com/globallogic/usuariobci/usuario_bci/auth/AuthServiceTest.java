package com.globallogic.usuariobci.usuario_bci.auth;


import com.globallogic.usuariobci.usuario_bci.jwt.JwtService;
import com.globallogic.usuariobci.usuario_bci.model.Role;
import com.globallogic.usuariobci.usuario_bci.model.Usuario;
import com.globallogic.usuariobci.usuario_bci.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;


    @Test
    void testRegister() {
        // Datos de prueba
        Usuario usuarioRequest = new Usuario();
        usuarioRequest.setUsername("usuario123");
        usuarioRequest.setPassword("A1a3asdrefty");
        usuarioRequest.setEmail("usuario@example.com");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setId(UUID.fromString("c07d68e5-481c-4e33-8ed4-344ce856f9b2"));
        usuarioGuardado.setUsername(usuarioRequest.getUsername());
        usuarioGuardado.setPassword("A1a3asdrefty"); // La contraseña se codificará
        usuarioGuardado.setEmail(usuarioRequest.getEmail());
        usuarioGuardado.setRole(Role.USER);

        // Configuración de comportamientos simulados
        when(passwordEncoder.encode(anyString())).thenReturn("A1a3asdrefty");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);
        when(jwtService.getToken(any(Usuario.class))).thenReturn("tokenGenerado");

        // Llamada al método de registro
        UsuarioResponse usuarioRegistrado = authService.register(usuarioRequest);

        // Verificaciones
        assertEquals(usuarioGuardado.getId(), usuarioRegistrado.getId());
        assertEquals(usuarioGuardado.getName(), usuarioRegistrado.getName());
        assertEquals(usuarioGuardado.getPassword(), usuarioRegistrado.getPassword());
        assertEquals(usuarioGuardado.getEmail(), usuarioRegistrado.getEmail());
        assertEquals("tokenGenerado", usuarioRegistrado.getToken());

        // Verifica que los métodos se llamaron correctamente
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(jwtService, times(1)).getToken(any(Usuario.class));
    }

}

