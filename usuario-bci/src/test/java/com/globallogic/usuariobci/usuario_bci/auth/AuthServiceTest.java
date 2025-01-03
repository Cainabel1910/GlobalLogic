package com.globallogic.usuariobci.usuario_bci.auth;


import com.globallogic.usuariobci.usuario_bci.jwt.JwtService;
import com.globallogic.usuariobci.usuario_bci.model.Role;
import com.globallogic.usuariobci.usuario_bci.model.Usuario;
import com.globallogic.usuariobci.usuario_bci.repository.UsuarioRepository;
import com.globallogic.usuariobci.usuario_bci.service.EmailValidationService;
import com.globallogic.usuariobci.usuario_bci.service.PasswordValidationService;
import com.globallogic.usuariobci.usuario_bci.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Mock
    private EmailValidationService emailValidationService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AuthController authController;

    @Mock
    private PasswordValidationService passwordValidationService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private AuthenticationManager authenticationManager;


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

    @Test
    void testRegisterBadEmail() {
        // Datos de prueba
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("usuario123");
        registerRequest.setPassword("A1a3asdrefty");
        registerRequest.setEmail("usuario@example");

        // Simula que la validación de correo falla
        when(emailValidationService.validateEmail(registerRequest.getEmail())).thenReturn(false);

        // Llamada al método de registro
        ResponseEntity<?> response = authController.register(registerRequest, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Suponiendo que la respuesta es un Map<String, Object> con el campo "error"
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        // Obtén el detalle del error
        List<Map<String, Object>> errorDetails = (List<Map<String, Object>>) responseBody.get("error");

        // Verifica que el detalle del error sea el esperado
        assertEquals("El correo electrónico no cumple con el formato requerido.", errorDetails.get(0).get("detail"));
    }


    @Test
    void testRegisterBadPassword() {
        // Datos de prueba
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("usuario123");
        registerRequest.setPassword("A15698PPPP3asdreftyA"); // Contraseña no válida (según validación)
        registerRequest.setEmail("usuario@example.com");

        // Simula que la validación de contraseña falla
        when(passwordValidationService.validatePassword(registerRequest.getPassword())).thenReturn(false);
        // No haya problemas con el correo electrónico
        when(emailValidationService.validateEmail(registerRequest.getEmail())).thenReturn(true);

        // Llamada al método de registro
        ResponseEntity<?> response = authController.register(registerRequest, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Suponiendo que la respuesta es un Map<String, Object> con el campo "error"
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        // Obtén el detalle del error
        List<Map<String, Object>> errorDetails = (List<Map<String, Object>>) responseBody.get("error");

        // Verifica que el detalle del error sea el esperado
        assertEquals("La contraseña no cumple con el formato requerido.", errorDetails.get(0).get("detail"));
    }

    @Test
    void testRegisterEmailRegistrado() {
        // Datos de prueba
        // Datos de prueba
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("usuario123");
        registerRequest.setPassword("A1a3asdrefty");
        registerRequest.setEmail("usuario@example.com");


        // No haya problemas con LA contraseña
        when(passwordValidationService.validatePassword(registerRequest.getPassword())).thenReturn(true);
        // No haya problemas con el correo electrónico
        when(emailValidationService.validateEmail(registerRequest.getEmail())).thenReturn(true);
        // Simula que el correo ya está registrado
        when(usuarioService.porEmail(registerRequest.getEmail())).thenReturn(Optional.of(new Usuario()));

        // Llamada al método de registro
        ResponseEntity<?> response = authController.register(registerRequest, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Suponiendo que la respuesta es un Map<String, Object> con el campo "error"
        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();

        // Obtén el detalle del error
        List<Map<String, Object>> errorDetails = (List<Map<String, Object>>) responseBody.get("error");

        // Verifica que el detalle del error sea el esperado
        assertEquals("El correo ya está registrado.", errorDetails.get(0).get("detail"));

        // Verifica que se llamó al servicio para verificar el correo
        verify(usuarioService, times(1)).porEmail(registerRequest.getEmail());
    }

    @Test
    void testLogin_CredencialesIncorrectas() {
        // Datos de prueba
        LoginRequest loginRequest = new LoginRequest("usuario@example.com", "A1a3asdrefty");

        // Simula que el usuario no está registrado (usuario no encontrado en el repositorio)
        when(usuarioRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        // Llamada al método de login (la autenticación fallará porque el usuario no existe)
        ResponseEntity<?> response = authController.login(loginRequest, bindingResult);

        // Verificaciones
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());  // Estado HTTP 400 (BAD_REQUEST)
        assertTrue(response.getBody().toString().contains("error"));  // Verifica que contiene la palabra "error" en el cuerpo de la respuesta

    }






}

