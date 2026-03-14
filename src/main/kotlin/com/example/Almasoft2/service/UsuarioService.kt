package com.example.cronograma.service

import com.example.cronograma.model.Usuario
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class UsuarioService(
    private val jdbcTemplate: JdbcTemplate
) {

    // REGISTRAR USUARIO
    fun registrar(usuario: Usuario): String {

        val sql = """
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
            sql,
            usuario.usuario_primer_nombre,
            usuario.usuario_segundo_nombre,
            usuario.usuario_primer_apellido,
            usuario.usuario_segundo_apellido,
            usuario.usuario_documento,
            usuario.usuario_correo,
            usuario.usuario_direccion,
            usuario.usuario_credencial
        )

        return "Usuario registrado"
    }

    // LOGIN
    fun login(correo: String, password: String): Usuario? {

        val sql = """
        SELECT * FROM usuario
        WHERE usuario_correo = ? AND usuario_credencial = ?
        """

        val usuarios = jdbcTemplate.query(
            sql,
            { rs, _ ->
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
            },
            correo,
            password
        )

        return if (usuarios.isNotEmpty()) usuarios[0] else null
    }

    // OBTENER TODOS LOS USUARIOS
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

    // OBTENER USUARIO POR ID
    fun obtenerUsuarioPorId(id: Int): Usuario? {

        val sql = "SELECT * FROM usuario WHERE usuario_id = ?"

        val usuarios = jdbcTemplate.query(
            sql,
            { rs, _ ->
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
            },
            id
        )

        return if (usuarios.isNotEmpty()) usuarios[0] else null
    }

    // ACTUALIZAR USUARIO
    fun actualizarUsuario(id: Int, usuario: Usuario): String {

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

        jdbcTemplate.update(
            sql,
            usuario.usuario_primer_nombre,
            usuario.usuario_segundo_nombre,
            usuario.usuario_primer_apellido,
            usuario.usuario_segundo_apellido,
            usuario.usuario_documento,
            usuario.usuario_correo,
            usuario.usuario_direccion,
            usuario.usuario_credencial,
            id
        )

        return "Usuario actualizado"
    }

    // ELIMINAR USUARIO
    fun eliminarUsuario(id: Int): String {

        val sql = "DELETE FROM usuario WHERE usuario_id = ?"

        jdbcTemplate.update(sql, id)

        return "Usuario eliminado"
    }
}