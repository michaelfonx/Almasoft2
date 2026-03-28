package com.example.cronograma.service

import com.example.cronograma.model.Pago
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class PagoService(
    private val jdbcTemplate: JdbcTemplate
) {

    fun obtenerPagos(): List<Pago> {

        val sql = "SELECT * FROM pago"

        return jdbcTemplate.query(sql) { rs, _ ->
            Pago(
                rs.getInt("pago_id"),
                rs.getString("pago_metodo"),
                rs.getString("pago_fecha"),
                rs.getInt("contrato_id")
            )
        }
    }

    fun obtenerPagoPorId(id: Int): Pago? {

        val sql = "SELECT * FROM pago WHERE pago_id = ?"

        return jdbcTemplate.query(sql, arrayOf(id)) { rs, _ ->
            Pago(
                rs.getInt("pago_id"),
                rs.getString("pago_metodo"),
                rs.getString("pago_fecha"),
                rs.getInt("contrato_id")
            )
        }.firstOrNull()
    }

    fun crearPago(pago: Pago): String {

        val sql = """
            INSERT INTO pago (pago_metodo, pago_fecha, contrato_id)
            VALUES (?, ?, ?)
        """

        jdbcTemplate.update(
            sql,
            pago.pago_metodo,
            pago.pago_fecha,
            pago.contrato_id
        )

        return "Pago creado correctamente"
    }

    fun actualizarPago(id: Int, pago: Pago): String {

        val sql = """
            UPDATE pago
            SET pago_metodo = ?, pago_fecha = ?, contrato_id = ?
            WHERE pago_id = ?
        """

        val filas = jdbcTemplate.update(
            sql,
            pago.pago_metodo,
            pago.pago_fecha,
            pago.contrato_id,
            id
        )

        return if (filas > 0) "Pago actualizado"
        else "Pago no encontrado"
    }

    fun eliminarPago(id: Int): String {

        val sql = "DELETE FROM pago WHERE pago_id = ?"

        val filas = jdbcTemplate.update(sql, id)

        return if (filas > 0) "Pago eliminado"
        else "Pago no encontrado"
    }


    fun obtenerPagosPorContrato(contratoId: Int): List<Pago> {

        val sql = "SELECT * FROM pago WHERE contrato_id = ?"

        return jdbcTemplate.query(sql, arrayOf(contratoId)) { rs, _ ->
            Pago(
                rs.getInt("pago_id"),
                rs.getString("pago_metodo"),
                rs.getString("pago_fecha"),
                rs.getInt("contrato_id")
            )
        }
    }
}