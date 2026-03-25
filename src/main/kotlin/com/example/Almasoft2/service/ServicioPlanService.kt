package com.example.cronograma.service

import com.example.cronograma.model.ServicioPlan
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class ServicioPlanService(
    private val jdbcTemplate: JdbcTemplate
) {

    fun obtenerTodos(): List<ServicioPlan> {

        val sql = "SELECT * FROM servicio_plan"

        return jdbcTemplate.query(sql) { rs, _ ->
            ServicioPlan(
                rs.getInt("servicio_id"),
                rs.getInt("plan_id")
            )
        }
    }

    fun obtenerPorId(servicioId: Int): ServicioPlan? {

        val sql = "SELECT * FROM servicio_plan WHERE servicio_id = ?"

        return jdbcTemplate.query(sql, arrayOf(servicioId)) { rs, _ ->
            ServicioPlan(
                rs.getInt("servicio_id"),
                rs.getInt("plan_id")
            )
        }.firstOrNull()
    }

    fun crear(relacion: ServicioPlan): String {

        val sql = """
            INSERT INTO servicio_plan (servicio_id, plan_id)
            VALUES (?, ?)
        """

        jdbcTemplate.update(
            sql,
            relacion.servicio_id,
            relacion.plan_id
        )

        return "Relación servicio-plan creada"
    }

    fun actualizar(servicioId: Int, relacion: ServicioPlan): String {

        val sql = """
            UPDATE servicio_plan
            SET plan_id = ?
            WHERE servicio_id = ?
        """

        val filas = jdbcTemplate.update(
            sql,
            relacion.plan_id,
            servicioId
        )

        return if (filas > 0) "Relación actualizada"
        else "No encontrada"
    }

    fun eliminar(servicioId: Int): String {

        val sql = "DELETE FROM servicio_plan WHERE servicio_id = ?"

        val filas = jdbcTemplate.update(sql, servicioId)

        return if (filas > 0) "Relación eliminada"
        else "No encontrada"
    }
}