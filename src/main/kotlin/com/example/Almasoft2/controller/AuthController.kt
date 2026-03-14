package com.example.cronograma.controller

import com.example.cronograma.model.*
import com.example.cronograma.security.JwtUtil
import com.example.cronograma.service.UsuarioService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val usuarioService: UsuarioService
) {

    private val jwtUtil = JwtUtil()

    @PostMapping("/registro")
    fun registrar(
        @RequestBody usuario: Usuario
    ): String {

        return usuarioService.registrar(usuario)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody login: LoginRequest
    ): LoginResponse {

        val usuario = usuarioService.login(
            login.usuario_correo,
            login.usuario_credencial
        ) ?: throw RuntimeException("Credenciales incorrectas")

        val token = jwtUtil.generarToken(usuario.usuario_correo)

        return LoginResponse(token)
    }

}