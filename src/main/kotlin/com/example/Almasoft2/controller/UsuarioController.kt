package com.example.cronograma.controller

import com.example.Almasoft2.model.Usuario
import com.example.Almasoft2.service.UsuarioService
import org.springframework.http.ResponseEntity
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
    fun obtenerUsuarioPorId(@PathVariable id: Int): Usuario? {
        return usuarioService.obtenerUsuarioPorId(id)
    }


    @PutMapping("/{id}")
    fun actualizarUsuario(
        @PathVariable id: Int,
        @RequestBody usuario: Usuario
    ): ResponseEntity<Map<String, String>> {

        val (status, mensaje) = usuarioService.actualizarUsuario(id, usuario)

        return ResponseEntity.status(status)
            .body(mapOf("mensaje" to mensaje))
    }


    @DeleteMapping("/{id}")
    fun eliminarUsuario(@PathVariable id: Int): ResponseEntity<Map<String, String>> {

        val (status, mensaje) = usuarioService.eliminarUsuario(id)

        return ResponseEntity.status(status)
            .body(mapOf("mensaje" to mensaje))
    }
}