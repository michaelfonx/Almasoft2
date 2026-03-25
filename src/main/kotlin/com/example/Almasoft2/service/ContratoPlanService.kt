package com.example.cronograma.service

import com.example.cronograma.model.ContratoPlan
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class ContratoPlanService(
    private val jdbcTemplate: JdbcTemplate
) {

    fun obtenerTodos(): List<ContratoPlan> {

        val sql = "SELECT * FROM contrato_plan"

        return jdbcTemplate.query(sql) { rs, _ ->
            ContratoPlan(
                rs.getInt("contrato_id"),
                rs.getInt("plan_id")
            )
        }
    }

    fun obtenerPorId(contratoId: Int): ContratoPlan? {

        val sql = "SELECT * FROM contrato_plan WHERE contrato_id = ?"

        return jdbcTemplate.query(sql, arrayOf(contratoId)) { rs, _ ->
            ContratoPlan(
                rs.getInt("contrato_id"),
                rs.getInt("plan_id")
            )
        }.firstOrNull()
    }

    fun crear(relacion: ContratoPlan): String {

        val sql = """
            INSERT INTO contrato_plan (contrato_id, plan_id)
            VALUES (?, ?)
        """

        jdbcTemplate.update(
            sql,
            relacion.contrato_id,
            relacion.plan_id
        )

        return "Relación contrato-plan creada"
    }

    fun actualizar(contratoId: Int, relacion: ContratoPlan): String {

        val sql = """
            UPDATE contrato_plan
            SET plan_id = ?
            WHERE contrato_id = ?
        """

        val filas = jdbcTemplate.update(
            sql,
            relacion.plan_id,
            contratoId
        )

        return if (filas > 0) "Relación actualizada"
        else "No encontrada"
    }

    fun eliminar(contratoId: Int): String {

        val sql = "DELETE FROM contrato_plan WHERE contrato_id = ?"

        val filas = jdbcTemplate.update(sql, contratoId)

        return if (filas > 0) "Relación eliminada"
        else "No encontrada"
    }
}