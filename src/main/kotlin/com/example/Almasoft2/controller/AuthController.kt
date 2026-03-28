package com.example.Almasoft2.controller

import com.example.Almasoft2.model.LoginRequest
import com.example.Almasoft2.model.LoginResponse
import com.example.Almasoft2.model.Usuario
import com.example.Almasoft2.service.UsuarioService
import com.example.cronograma.security.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val usuarioService: UsuarioService
) {

    private val jwtUtil = JwtUtil()

    @PostMapping("/login")
    fun login(@RequestBody login: LoginRequest): LoginResponse {

        val usuario = usuarioService.login(
            login.usuario_correo,
            login.usuario_credencial
        ) ?: throw RuntimeException("Credenciales incorrectas")

        val token = jwtUtil.generarToken(usuario.usuario_correo)

        val usuarioId = usuario.usuario_id ?: throw RuntimeException("Usuario sin ID")

        val rolId = usuarioService.obtenerRolIdPorUsuario(usuarioId)

        val rol = try {
            usuarioService.obtenerNombreRol(rolId)
        } catch (e: Exception) {
            "CLIENTE"
        }

        val clienteId = try {
            usuarioService.obtenerClienteIdPorUsuario(usuarioId)
        } catch (e: Exception) {
            null
        }

        return LoginResponse(
            token = token,
            rol = rol,
            usuario_id = usuarioId,
            cliente_id = clienteId,
            usuario_primer_nombre = usuario.usuario_primer_nombre,
            usuario_segundo_nombre = usuario.usuario_segundo_nombre ?: "",
            usuario_primer_apellido = usuario.usuario_primer_apellido,
            usuario_segundo_apellido = usuario.usuario_segundo_apellido ?: "",
            usuario_correo = usuario.usuario_correo
        )
    }
    @PostMapping("/registro")
    fun registrar(@RequestBody request: Map<String, Any>): ResponseEntity<Any> {

        try {

            val usuario = Usuario(
                usuario_primer_nombre = request["usuario_primer_nombre"].toString(),
                usuario_segundo_nombre = request["usuario_segundo_nombre"]?.toString() ?: "",
                usuario_primer_apellido = request["usuario_primer_apellido"].toString(),
                usuario_segundo_apellido = request["usuario_segundo_apellido"]?.toString() ?: "",
                usuario_documento = (request["usuario_documento"] as Number).toInt(),
                usuario_correo = request["usuario_correo"].toString(),
                usuario_direccion = request["usuario_direccion"].toString(),
                fecha_nacimiento = request["fecha_nacimiento"]?.toString() ?: "",
                usuario_credencial = request["usuario_credencial"].toString()
            )

            val rolId = (request["rol_id"] as? Number)?.toInt() ?: 1

            val result = usuarioService.registrar(usuario, rolId)

            return ResponseEntity.ok(mapOf("mensaje" to result))

        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.badRequest()
                .body(mapOf("error" to (e.message ?: "Error al registrar")))
        }
    }
}