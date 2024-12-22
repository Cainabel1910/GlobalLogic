package com.globallogic.usuariobci.usuario_bci.service;

import com.globallogic.usuariobci.usuario_bci.model.Usuario;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioService {
    List<Usuario> listar();
    Optional<Usuario> porId(UUID id);
    Usuario guardar(Usuario usuario);
    void eliminar(UUID id);
    Optional<Usuario> porEmail(String email);
}
