package com.example.cronograma.service

import com.example.Almasoft2.model.Servicio
import com.example.cronograma.model.DTO.DetallePlanDTO
import com.example.cronograma.model.Plan
import com.example.cronograma.model.Producto
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
                rs.getBoolean("plan_estado"),
                rs.getString("plan_descripcion")
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
                rs.getBoolean("plan_estado"),
                rs.getString("plan_descripcion")
            )
        }.firstOrNull()
    }

    fun obtenerDetallePlan(id: Int): DetallePlanDTO? {

        val plan = obtenerPlanPorId(id) ?: return null

        val sqlServicios = """
            SELECT s.servicio_id, s.servicio_nombre, s.servicio_descripcion, s.servicio_precio
            FROM servicio s
            INNER JOIN servicio_plan sp ON s.servicio_id = sp.servicio_id
            WHERE sp.plan_id = ?
        """

        val servicios = try {
            jdbcTemplate.query(sqlServicios, arrayOf(id)) { rs, _ ->
                Servicio(
                    servicioId = rs.getLong("servicio_id"),
                    servicioNombre = rs.getString("servicio_nombre"),
                    servicioDescripcion = rs.getString("servicio_descripcion"),
                    servicioPrecio = rs.getDouble("servicio_precio")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }

        val sqlProductos = """
            SELECT p.*
            FROM producto p
            INNER JOIN plan_producto pp ON p.producto_id = pp.producto_id
            WHERE pp.plan_id = ?
        """

        val productos = try {
            jdbcTemplate.query(sqlProductos, arrayOf(id)) { rs, _ ->
                Producto(
                    rs.getInt("producto_id"),
                    rs.getString("producto_nombre"),
                    rs.getString("producto_descripcion"),
                    rs.getDouble("producto_precio"),
                    rs.getInt("producto_stock"),
                    rs.getBoolean("producto_estado"),
                    rs.getInt("subcategoria_id")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }

        return DetallePlanDTO(plan, servicios, productos)
    }
    fun crearPlan(plan: Plan): String {
        val sql = """
        INSERT INTO PLAN_FUNEBRE (plan_nombre, plan_precio, plan_estado, plan_descripcion)
        VALUES (?, ?, ?, ?)
    """

        jdbcTemplate.update(
            sql,
            plan.plan_nombre,
            plan.plan_precio,
            plan.plan_estado,
            plan.plan_descripcion
        )

        return "Plan creado"
    }

    fun actualizarPlan(id: Int, plan: Plan): String {
        val sql = """
        UPDATE PLAN_FUNEBRE
        SET plan_nombre = ?, plan_precio = ?, plan_estado = ?, plan_descripcion = ?
        WHERE plan_id = ?
    """

        val filas = jdbcTemplate.update(
            sql,
            plan.plan_nombre,
            plan.plan_precio,
            plan.plan_estado,
            plan.plan_descripcion,
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