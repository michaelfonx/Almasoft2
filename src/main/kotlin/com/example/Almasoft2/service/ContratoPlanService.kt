package com.example.cronograma.service

import com.example.cronograma.dto.PagoDTO
import com.example.cronograma.model.DTO.MiPlanDTO
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class ContratoPlanService(
    private val jdbcTemplate: JdbcTemplate
) {

    fun obtenerMiPlan(clienteId: Int): MiPlanDTO? {

        val contratoSql = """
            SELECT c.contrato_id, p.plan_id, p.plan_nombre, p.plan_precio, p.plan_descripcion
            FROM CONTRATO c
            LEFT JOIN CONTRATO_PLAN cp ON c.contrato_id = cp.contrato_id
            LEFT JOIN PLAN_FUNEBRE p ON cp.plan_id = p.plan_id
            WHERE c.cliente_id = ?
            LIMIT 1
        """

        val contrato = jdbcTemplate.query(contratoSql, arrayOf(clienteId)) { rs, _ ->
            mapOf(
                "contrato_id" to rs.getInt("contrato_id"),
                "plan_id" to rs.getInt("plan_id"),
                "plan_nombre" to rs.getString("plan_nombre"),
                "plan_precio" to rs.getDouble("plan_precio"),
                "plan_descripcion" to rs.getString("plan_descripcion")
            )
        }.firstOrNull() ?: return null

        val contratoId = contrato["contrato_id"] as Int
        val planId = contrato["plan_id"] as Int

        // 🔹 SERVICIOS DEL PLAN
        val servicios = jdbcTemplate.query(
            """
            SELECT s.servicio_nombre 
            FROM SERVICIO s
            INNER JOIN SERVICIO_PLAN sp ON s.servicio_id = sp.servicio_id
            WHERE sp.plan_id = ?
            """,
            arrayOf(planId)
        ) { rs, _ -> rs.getString("servicio_nombre") }

        // 🔹 PRODUCTOS DEL CONTRATO
        val productos = jdbcTemplate.query(
            """
            SELECT p.producto_nombre
            FROM PRODUCTO p
            INNER JOIN CONTRATO_PRODUCTO cp ON p.producto_id = cp.producto_id
            WHERE cp.contrato_id = ?
            """,
            arrayOf(contratoId)
        ) { rs, _ -> rs.getString("producto_nombre") }

        val pagos = jdbcTemplate.query(
            """
            SELECT pago_metodo, pago_fecha
            FROM PAGO
            WHERE contrato_id = ?
            """,
            arrayOf(contratoId)
        ) { rs, _ ->
            PagoDTO(
                metodo = rs.getString("pago_metodo"),
                fecha = rs.getString("pago_fecha")
            )
        }

        return MiPlanDTO(
            contrato_id = contratoId,
            plan_nombre = contrato["plan_nombre"] as String?,
            plan_precio = contrato["plan_precio"] as Double?,
            plan_descripcion = contrato["plan_descripcion"] as String?,
            servicios = servicios,
            productos = productos,
            pagos = pagos
        )
    }
}