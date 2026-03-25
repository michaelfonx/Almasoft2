package com.example.cronograma.service

import com.example.cronograma.model.Plan
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class PlanService(
    private val jdbcTemplate: JdbcTemplate
) {

    fun obtenerPlanes(): List<Plan> {
        val sql = "SELECT * FROM PLAN_FUNEBRE"

        return jdbcTemplate.query(sql) { rs, _ ->
            Plan(
                rs.getInt("plan_id"),
                rs.getString("plan_nombre"),
                rs.getDouble("plan_precio"),
                rs.getBoolean("plan_estado")
            )
        }
    }

    fun obtenerPlanPorId(id: Int): Plan? {
        val sql = "SELECT * FROM PLAN_FUNEBRE WHERE plan_id = ?"

        return jdbcTemplate.query(sql, arrayOf(id)) { rs, _ ->
            Plan(
                rs.getInt("plan_id"),
                rs.getString("plan_nombre"),
                rs.getDouble("plan_precio"),
                rs.getBoolean("plan_estado")
            )
        }.firstOrNull()
    }

    fun crearPlan(plan: Plan): String {
        val sql = """
            INSERT INTO PLAN_FUNEBRE (plan_nombre, plan_precio, plan_estado)
            VALUES (?, ?, ?)
        """

        jdbcTemplate.update(
            sql,
            plan.plan_nombre,
            plan.plan_precio,
            plan.plan_estado
        )

        return "Plan creado"
    }

    fun actualizarPlan(id: Int, plan: Plan): String {
        val sql = """
            UPDATE PLAN_FUNEBRE
            SET plan_nombre = ?, plan_precio = ?, plan_estado = ?
            WHERE plan_id = ?
        """

        val filas = jdbcTemplate.update(
            sql,
            plan.plan_nombre,
            plan.plan_precio,
            plan.plan_estado,
            id
        )

        return if (filas > 0) "Plan actualizado"
        else "Plan no encontrado"
    }

    fun eliminarPlan(id: Int): String {
        val sql = "DELETE FROM PLAN_FUNEBRE WHERE plan_id = ?"

        val filas = jdbcTemplate.update(sql, id)

        return if (filas > 0) "Plan eliminado"
        else "Plan no encontrado"
    }
}