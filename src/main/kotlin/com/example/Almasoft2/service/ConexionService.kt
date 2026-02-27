package com.example.cronograma.service

import com.example.cronograma.model.Contrato
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class ConexionService(
    private val jdbcTemplate: JdbcTemplate
) {

    fun obtenerContratos(): List<Contrato> {

        val sql = "SELECT * FROM contrato"

        return jdbcTemplate.query(sql) { rs, _ ->
            Contrato(
                rs.getInt("contrato_id"),
                rs.getBoolean("contrato_estado"),
                rs.getDouble("contrato_valor"),
                rs.getInt("cliente_id")
            )
        }
    }

    fun obtenerContratoPorId(id: Int): Contrato? {

        val sql = "SELECT * FROM contrato WHERE contrato_id = ?"

        return jdbcTemplate.query(sql, arrayOf(id)) { rs, _ ->
            Contrato(
                rs.getInt("contrato_id"),
                rs.getBoolean("contrato_estado"),
                rs.getDouble("contrato_valor"),
                rs.getInt("cliente_id")
            )
        }.firstOrNull()
    }

    fun crearContrato(contrato: Contrato): String {

        val sql = """
            INSERT INTO contrato (cliente_id, contrato_estado, contrato_valor)
            VALUES (?, ?, ?)
        """

        jdbcTemplate.update(
            sql,
            contrato.cliente_id,
            contrato.contrato_estado,
            contrato.contrato_valor
        )

        return "Contrato creado correctamente"
    }

    fun actualizarContrato(id: Int, contrato: Contrato): String {

        val sql = """
            UPDATE contrato
            SET cliente_id = ?, contrato_estado = ?, contrato_valor = ?
            WHERE contrato_id = ?
        """

        val filas = jdbcTemplate.update(
            sql,
            contrato.cliente_id,
            contrato.contrato_estado,
            contrato.contrato_valor,
            id
        )

        return if (filas > 0) "Contrato actualizado"
        else "Contrato no encontrado"
    }

    fun eliminarContrato(id: Int): String {

        val sql = "DELETE FROM contrato WHERE contrato_id = ?"

        val filas = jdbcTemplate.update(sql, id)

        return if (filas > 0) "Contrato eliminado"
        else "Contrato no encontrado"
    }
}

