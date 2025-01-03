package com.globallogic.usuariobci.usuario_bci.auth;


import com.globallogic.usuariobci.usuario_bci.model.Usuario;
import com.globallogic.usuariobci.usuario_bci.service.EmailValidationService;
import com.globallogic.usuariobci.usuario_bci.service.PasswordValidationService;
import com.globallogic.usuariobci.usuario_bci.service.UsuarioService;
import org.springframework.validation.BindingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordValidationService passwordValidationService;
    @Autowired
    private EmailValidationService emailValidationService;
    @Autowired
    private UsuarioService usuarioService;


    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, BindingResult result) {
        // Verificamos si el correo se encuentra registrado
        if (!usuarioService.porEmail(request.getEmail()).isPresent()) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "Credenciales Incorrectas.");
        }
        try{
            return ResponseEntity.ok(authService.login(request));
        } catch (Exception e){
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "Error al loguearse.");
        }
    }

    @PostMapping(value = "sing-up")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request, BindingResult result) {
        //Verificamos que los datos esten correctos
        if (result.hasErrors()){
            return validar(result);
        }
        // Validamos la estructura de correo
        if (!emailValidationService.validateEmail(request.getEmail())) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "El correo electrónico no cumple con el formato requerido.");
        }
        // Verificamos si la contraseña cumple con el formato
        if (!passwordValidationService.validatePassword(request.getPassword())) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "La contraseña no cumple con el formato requerido.");
        }

        // Verificamos si el correo se encuentra registrado
        if (usuarioService.porEmail(request.getEmail()).isPresent()) {
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "El correo ya está registrado.");
        }

        Usuario usuarioAux = new Usuario();
        usuarioAux.setName(request.getName());
        usuarioAux.setEmail(request.getEmail());
        usuarioAux.setPassword(request.getPassword());
        usuarioAux.setPhones(request.getPhones());
        return ResponseEntity.ok(authService.register(usuarioAux));
    }

    private static ResponseEntity<Map<String, Object>> validar(BindingResult result) {
        Map<String, Object> errorResponse = new HashMap<>();
        List<Map<String, Object>> errorDetails = new ArrayList<>();

        result.getFieldErrors().forEach(err -> {
            Map<String, Object> detail = new HashMap<>();
            detail.put("field", err.getField()); // Campo con error
            detail.put("message", "El campo " + err.getField() + " " + err.getDefaultMessage()); // Mensaje de error
            detail.put("timestamp", Instant.now()); // Timestamp para cada error
            errorDetails.add(detail);
        });

        errorResponse.put("error", errorDetails); // Lista de errores
        errorResponse.put("codigo", HttpStatus.BAD_REQUEST.value()); // Código de estado HTTP

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    private ResponseEntity<Map<String, Object>> buildErrorResponse(HttpStatus status, String detail) {
        Map<String, Object> errorResponse = new HashMap<>();
        Map<String, Object> errorDetail = new HashMap<>();

        errorDetail.put("timestamp", Instant.now());
        errorDetail.put("codigo", status.value());
        errorDetail.put("detail", detail);

        errorResponse.put("error", Collections.singletonList(errorDetail));

        return ResponseEntity.status(status).body(errorResponse);
    }
}
