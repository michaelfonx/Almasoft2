package com.example.Almasoft2.service

import com.example.Almasoft2.model.Usuario
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsuarioService(
    private val jdbcTemplate: JdbcTemplate
) {

    private val encoder = BCryptPasswordEncoder()


    fun registrar(usuario: Usuario, rolId: Int): String {

        val passwordEncriptada = encoder.encode(usuario.usuario_credencial)

        val sqlUsuario = """
        INSERT INTO usuario(
            usuario_primer_nombre,
            usuario_segundo_nombre,
            usuario_primer_apellido,
            usuario_segundo_apellido,
            usuario_documento,
            usuario_correo,
            usuario_direccion,
            usuario_credencial
        )
        VALUES (?,?,?,?,?,?,?,?)
        """

        jdbcTemplate.update(
            sqlUsuario,
            usuario.usuario_primer_nombre,
            usuario.usuario_segundo_nombre,
            usuario.usuario_primer_apellido,
            usuario.usuario_segundo_apellido,
            usuario.usuario_documento,
            usuario.usuario_correo,
            usuario.usuario_direccion,
            passwordEncriptada
        )

        val usuarioId = jdbcTemplate.queryForObject(
            "SELECT LAST_INSERT_ID()",
            Int::class.java
        )!!

        jdbcTemplate.update(
            "INSERT INTO rol_usuario (rol_id, usuario_id, estado_cred) VALUES (?, ?, 1)",
            rolId, usuarioId
        )

        return "Usuario registrado"
    }


    fun login(correo: String, password: String): Usuario? {

        val sql = "SELECT * FROM usuario WHERE usuario_correo = ?"

        val usuarios = jdbcTemplate.query(sql, { rs, _ ->
            Usuario(
                rs.getInt("usuario_id"),
                rs.getString("usuario_primer_nombre"),
                rs.getString("usuario_segundo_nombre"),
                rs.getString("usuario_primer_apellido"),
                rs.getString("usuario_segundo_apellido"),
                rs.getInt("usuario_documento"),
                rs.getString("usuario_correo"),
                rs.getString("usuario_direccion"),
                rs.getString("usuario_credencial")
            )
        }, correo)

        val usuario = usuarios.firstOrNull() ?: return null

        return if (encoder.matches(password, usuario.usuario_credencial)) usuario else null
    }


    fun obtenerRolIdPorUsuario(usuarioId: Int): Int {
        val sql = "SELECT rol_id FROM rol_usuario WHERE usuario_id = ?"
        return jdbcTemplate.queryForObject(sql, Int::class.java, usuarioId) ?: 1
    }


    fun obtenerNombreRol(rolId: Int): String {
        val sql = "SELECT rol_nombre FROM rol WHERE rol_id = ?"
        return jdbcTemplate.queryForObject(sql, String::class.java, rolId) ?: "CLIENTE"
    }


    fun obtenerUsuarios(): List<Usuario> {
        val sql = "SELECT * FROM usuario"
        return jdbcTemplate.query(sql) { rs, _ ->
            Usuario(
                rs.getInt("usuario_id"),
                rs.getString("usuario_primer_nombre"),
                rs.getString("usuario_segundo_nombre"),
                rs.getString("usuario_primer_apellido"),
                rs.getString("usuario_segundo_apellido"),
                rs.getInt("usuario_documento"),
                rs.getString("usuario_correo"),
                rs.getString("usuario_direccion"),
                rs.getString("usuario_credencial")
            )
        }
    }


    fun obtenerUsuarioPorId(id: Int): Usuario? {
        val sql = "SELECT * FROM usuario WHERE usuario_id = ?"

        val lista = jdbcTemplate.query(sql, { rs, _ ->
            Usuario(
                rs.getInt("usuario_id"),
                rs.getString("usuario_primer_nombre"),
                rs.getString("usuario_segundo_nombre"),
                rs.getString("usuario_primer_apellido"),
                rs.getString("usuario_segundo_apellido"),
                rs.getInt("usuario_documento"),
                rs.getString("usuario_correo"),
                rs.getString("usuario_direccion"),
                rs.getString("usuario_credencial")
            )
        }, id)

        return lista.firstOrNull()
    }


    fun actualizarUsuario(id: Int, usuario: Usuario): Pair<Int, String> {


        val actual = obtenerUsuarioPorId(id)
            ?: return 404 to "Usuario no encontrado"

        val passwordFinal = if (usuario.usuario_credencial.isNotEmpty()) {
            encoder.encode(usuario.usuario_credencial) // ENCRIPTAR
        } else {
            actual.usuario_credencial
        }

        val sql = """
    UPDATE usuario SET
        usuario_primer_nombre = ?,
        usuario_segundo_nombre = ?,
        usuario_primer_apellido = ?,
        usuario_segundo_apellido = ?,
        usuario_documento = ?,
        usuario_correo = ?,
        usuario_direccion = ?,
        usuario_credencial = ?
    WHERE usuario_id = ?
    """

        val filas = jdbcTemplate.update(
            sql,
            usuario.usuario_primer_nombre,
            usuario.usuario_segundo_nombre,
            usuario.usuario_primer_apellido,
            usuario.usuario_segundo_apellido,
            usuario.usuario_documento,
            usuario.usuario_correo,
            usuario.usuario_direccion,
            passwordFinal, // 🔥 AQUÍ VA LA ENCRIPTADA
            id
        )

        return if (filas > 0) {
            200 to "Usuario actualizado"
        } else {
            404 to "Usuario no encontrado"
        }
    }


    fun eliminarUsuario(id: Int): Pair<Int, String> {

        val filas = jdbcTemplate.update(
            "DELETE FROM usuario WHERE usuario_id = ?",
            id
        )

        return if (filas > 0) {
            200 to "Usuario eliminado"
        } else {
            404 to "Usuario no encontrado"
        }
    }
}