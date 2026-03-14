package com.example.cronograma.controller

import com.example.cronograma.model.Usuario
import com.example.cronograma.service.UsuarioService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/usuarios")
class UsuarioController(
    private val usuarioService: UsuarioService
) {

    @GetMapping
    fun obtenerUsuarios(): List<Usuario> {
        return usuarioService.obtenerUsuarios()
    }

    @GetMapping("/{id}")
    fun obtenerUsuarioPorId(
        @PathVariable id: Int
    ): Usuario? {

        return usuarioService.obtenerUsuarioPorId(id)
    }

    @PutMapping("/{id}")
    fun actualizarUsuario(
        @PathVariable id: Int,
        @RequestBody usuario: Usuario
    ): String {

        return usuarioService.actualizarUsuario(id, usuario)
    }

    @DeleteMapping("/{id}")
    fun eliminarUsuario(
        @PathVariable id: Int
    ): String {

        return usuarioService.eliminarUsuario(id)
    }
}