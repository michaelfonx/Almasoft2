package com.example.cronograma.service

import com.example.cronograma.model.Contrato
import com.example.cronograma.dto.PagoDTO
import com.example.cronograma.model.DTO.AfiliadoDTO
import com.example.cronograma.model.DTO.MiPlanDTO
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

        val contrato = jdbcTemplate.query(
            """
        SELECT c.contrato_id, p.plan_id, p.plan_nombre, p.plan_precio, p.plan_descripcion
        FROM contrato c
        JOIN contrato_plan cp ON c.contrato_id = cp.contrato_id
        JOIN plan_funebre p ON cp.plan_id = p.plan_id
        WHERE c.cliente_id = ?
        LIMIT 1
        """, arrayOf(clienteId)
        ) { rs, _ ->
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


        val servicios = jdbcTemplate.query(
            """
        SELECT s.servicio_nombre
        FROM servicio s
        JOIN servicio_plan sp ON s.servicio_id = sp.servicio_id
        WHERE sp.plan_id = ?
        """, arrayOf(planId)
        ) { rs, _ ->
            rs.getString("servicio_nombre")
        }


        val productos = jdbcTemplate.query(
            """
        SELECT p.producto_nombre
        FROM producto p
        JOIN contrato_producto cp ON p.producto_id = cp.producto_id
        WHERE cp.contrato_id = ?
        """, arrayOf(contratoId)
        ) { rs, _ ->
            rs.getString("producto_nombre")
        }


        val pagos = jdbcTemplate.query(
            """
        SELECT pago_metodo, pago_fecha
        FROM pago
        WHERE contrato_id = ?
        """, arrayOf(contratoId)
        ) { rs, _ ->
            com.example.cronograma.dto.PagoDTO(
                metodo = rs.getString("pago_metodo"),
                fecha = rs.getString("pago_fecha")
            )
        }

        return MiPlanDTO(
            contrato_id = contratoId,
            plan_nombre = contrato["plan_nombre"] as String,
            plan_precio = contrato["plan_precio"] as Double,
            plan_descripcion = contrato["plan_descripcion"] as String,
            servicios = servicios,
            productos = productos,
            pagos = pagos
        )
    }

    fun crearContrato(contrato: Contrato): Int {

        val existente = obtenerContratoPorCliente(contrato.cliente_id)

        if (existente != null) {
            return existente.contrato_id!!
        }

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

    fun adquirirPlan(clienteId: Int, planId: Int, valor: Double): Int {

        val contratoExistente = obtenerContratoPorCliente(clienteId)

        val contratoId = if (contratoExistente != null) {
            contratoExistente.contrato_id!!
        } else {

            jdbcTemplate.update(
                """
            INSERT INTO contrato (cliente_id, contrato_estado, contrato_valor)
            VALUES (?, ?, ?)
            """,
                clienteId, true, valor
            )

            jdbcTemplate.queryForObject(
                "SELECT LAST_INSERT_ID()",
                Int::class.java
            )!!
        }

        val existeRelacion = jdbcTemplate.query(
            "SELECT * FROM contrato_plan WHERE contrato_id = ?",
            arrayOf(contratoId)
        ) { _, _ -> 1 }.isNotEmpty()

        if (!existeRelacion) {
            jdbcTemplate.update(
                "INSERT INTO contrato_plan (contrato_id, plan_id) VALUES (?, ?)",
                contratoId,
                planId
            )
        }


        val productos = jdbcTemplate.query(
            "SELECT producto_id FROM producto WHERE producto_estado = 1"
        ) { rs, _ -> rs.getInt("producto_id") }

        productos.forEach { productoId ->

            val existe = jdbcTemplate.query(
                "SELECT * FROM contrato_producto WHERE contrato_id = ? AND producto_id = ?",
                arrayOf(contratoId, productoId)
            ) { _, _ -> 1 }.isNotEmpty()

            if (!existe) {
                jdbcTemplate.update(
                    "INSERT INTO contrato_producto (contrato_id, producto_id) VALUES (?, ?)",
                    contratoId,
                    productoId
                )
            }
        }

        return contratoId
    }

    fun agregarAfiliado(contratoId: Int, usuarioId: Int): String {

        val existe = jdbcTemplate.query(
            "SELECT * FROM afiliado WHERE afiliado_id = ? AND contrato_id = ?",
            arrayOf(usuarioId, contratoId)
        ) { _, _ -> 1 }.isNotEmpty()

        if (existe) return "Ya está afiliado"

        jdbcTemplate.update(
            "INSERT INTO afiliado (afiliado_id, contrato_id) VALUES (?, ?)",
            usuarioId, contratoId
        )

        return "Afiliado agregado"
    }

    fun obtenerAfiliados(contratoId: Int): List<AfiliadoDTO> {

        return jdbcTemplate.query(
            """
        SELECT u.usuario_id, u.usuario_primer_nombre, u.usuario_primer_apellido
        FROM afiliado a
        JOIN usuario u ON a.afiliado_id = u.usuario_id
        WHERE a.contrato_id = ?
        """,
            arrayOf(contratoId)
        ) { rs, _ ->
            AfiliadoDTO(
                usuario_id = rs.getInt("usuario_id"),
                nombre = rs.getString("usuario_primer_nombre"),
                apellido = rs.getString("usuario_primer_apellido")
            )
        }

    }

    fun agregarAfiliadoPorDocumento(contratoId: Int, documento: Int): String {

        if (contratoId == 0) return "Contrato inválido"

        val usuarioId = jdbcTemplate.query(
            """
        SELECT usuario_id 
        FROM usuario 
        WHERE usuario_documento = ?
        LIMIT 1
        """.trimIndent(),
            arrayOf(documento)
        ) { rs, _ -> rs.getInt("usuario_id") }
            .firstOrNull()

        if (usuarioId == null) {
            return "Usuario no encontrado"
        }

        val existe = jdbcTemplate.query(
            """
        SELECT 1 
        FROM afiliado 
        WHERE afiliado_id = ? AND contrato_id = ?
        LIMIT 1
        """.trimIndent(),
            arrayOf(usuarioId, contratoId)
        ) { _, _ -> 1 }.isNotEmpty()

        if (existe) return "Ya está afiliado"

        return try {
            jdbcTemplate.update(
                """
            INSERT INTO afiliado (afiliado_id, contrato_id) 
            VALUES (?, ?)
            """.trimIndent(),
                usuarioId,
                contratoId
            )
            "Afiliado agregado"
        } catch (e: Exception) {
            "Error al insertar"
        }
    }
    fun agregarProductoContrato(contratoId: Int, productoId: Int): String {

        val existe = jdbcTemplate.query(
            "SELECT 1 FROM contrato_producto WHERE contrato_id = ? AND producto_id = ?",
            arrayOf(contratoId, productoId)
        ) { _, _ -> 1 }.isNotEmpty()

        if (existe) return "Producto ya agregado al contrato"

        jdbcTemplate.update(
            "INSERT INTO contrato_producto (contrato_id, producto_id) VALUES (?, ?)",
            contratoId,
            productoId
        )

        return "Producto agregado al contrato"
    }
    fun agregarProductoAContrato(contratoId: Int, productoId: Int): String {

        val existe = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM contrato_producto WHERE contrato_id = ? AND producto_id = ?",
            Int::class.java,
            contratoId,
            productoId
        ) ?: 0

        if (existe > 0) {
            return "Producto ya agregado al contrato"
        }

        jdbcTemplate.update(
            "INSERT INTO contrato_producto (contrato_id, producto_id) VALUES (?, ?)",
            contratoId,
            productoId
        )

        return "Producto agregado al contrato"
    }
}