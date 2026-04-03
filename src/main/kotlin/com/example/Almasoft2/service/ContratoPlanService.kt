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

        val sql = """
            SELECT c.contrato_id, p.plan_nombre, p.plan_precio, p.plan_descripcion
            FROM CONTRATO c
            LEFT JOIN CONTRATO_PLAN cp ON c.contrato_id = cp.contrato_id
            LEFT JOIN PLAN_FUNEBRE p ON cp.plan_id = p.plan_id
            WHERE c.cliente_id = ?
            LIMIT 1
        """

        return jdbcTemplate.query(sql, arrayOf(clienteId)) { rs, _ ->
            MiPlanDTO(
                contrato_id = rs.getInt("contrato_id"),
                plan_nombre = rs.getString("plan_nombre") ?: "Sin nombre",
                plan_precio = rs.getDouble("plan_precio"),
                plan_descripcion = rs.getString("plan_descripcion") ?: "Sin descripción",
                servicios = emptyList(),
                productos = emptyList(),
                pagos = emptyList<PagoDTO>()
            )
        }.firstOrNull()
    }
}