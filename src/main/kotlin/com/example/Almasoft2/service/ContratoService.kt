package com.example.cronograma.service

import com.example.cronograma.model.Contrato
import com.example.cronograma.dto.PagoDTO
import com.example.cronograma.dto.MiPlanDTO
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class ContratoService(
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

    fun crearContrato(contrato: Contrato): Int {

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

        return jdbcTemplate.queryForObject(
            "SELECT LAST_INSERT_ID()",
            Int::class.java
        )!!
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

    fun obtenerContratoPorCliente(clienteId: Int): Contrato? {

        val sql = "SELECT * FROM contrato WHERE cliente_id = ? LIMIT 1"

        return jdbcTemplate.query(sql, arrayOf(clienteId)) { rs, _ ->
            Contrato(
                rs.getInt("contrato_id"),
                rs.getBoolean("contrato_estado"),
                rs.getDouble("contrato_valor"),
                rs.getInt("cliente_id")
            )
        }.firstOrNull()
    }


    fun obtenerMiPlan(clienteId: Int): MiPlanDTO? {

        val contrato = obtenerContratoPorCliente(clienteId) ?: return null


        val planSql = """
        SELECT p.plan_id, p.plan_nombre, p.plan_precio, p.plan_descripcion
        FROM contrato_plan cp
        JOIN plan_funebre p ON cp.plan_id = p.plan_id
        WHERE cp.contrato_id = ?
        LIMIT 1
    """

        val plan = jdbcTemplate.query(planSql, arrayOf(contrato.contrato_id)) { rs, _ ->
            listOf(
                rs.getInt("plan_id"),
                rs.getString("plan_nombre"),
                rs.getDouble("plan_precio"),
                rs.getString("plan_descripcion")
            )
        }.firstOrNull() ?: return null

        val planId = plan[0] as Int
        val planNombre = plan[1] as String
        val planPrecio = plan[2] as Double
        val planDescripcion = plan[3] as String


        val serviciosSql = """
        SELECT s.servicio_nombre
        FROM servicio_plan sp
        JOIN servicio s ON sp.servicio_id = s.servicio_id
        WHERE sp.plan_id = ?
    """

        val servicios = jdbcTemplate.query(serviciosSql, arrayOf(planId)) { rs, _ ->
            rs.getString("servicio_nombre")
        }

        val productosSql = """
        SELECT pr.producto_nombre
        FROM contrato_producto cp
        JOIN producto pr ON cp.producto_id = pr.producto_id
        WHERE cp.contrato_id = ?
    """

        val productos = jdbcTemplate.query(productosSql, arrayOf(contrato.contrato_id)) { rs, _ ->
            rs.getString("producto_nombre")
        }


        val pagosSql = """
        SELECT pago_metodo, pago_fecha
        FROM pago
        WHERE contrato_id = ?
    """

        val pagos = jdbcTemplate.query(pagosSql, arrayOf(contrato.contrato_id)) { rs, _ ->
            PagoDTO(
                metodo = rs.getString("pago_metodo"),
                fecha = rs.getString("pago_fecha")
            )
        }

        return MiPlanDTO(
            contrato_id = contrato.contrato_id!!,
            plan_nombre = planNombre,
            plan_precio = planPrecio,
            plan_descripcion = planDescripcion,
            servicios = servicios,
            productos = productos,
            pagos = pagos
        )
    }
}