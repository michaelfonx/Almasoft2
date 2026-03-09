package com.example.cronograma.service

import com.example.cronograma.model.Usuario
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class UsuarioService(
    private val jdbcTemplate: JdbcTemplate
) {

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
}
