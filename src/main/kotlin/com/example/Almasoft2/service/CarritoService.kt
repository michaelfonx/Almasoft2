package com.example.cronograma.service

import com.example.cronograma.model.Carrito
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class CarritoService(
    private val jdbcTemplate: JdbcTemplate
) {

    fun obtenerCarrito(usuarioId: Int): List<Carrito> {

        val sql = "SELECT * FROM carrito WHERE usuario_id = ?"

        return jdbcTemplate.query(sql, arrayOf(usuarioId)) { rs, _ ->
            Carrito(
                rs.getInt("carrito_id"),
                rs.getInt("usuario_id"),
                rs.getInt("producto_id"),
                rs.getInt("cantidad"),
                rs.getDouble("precio_unitario")
            )
        }
    }

    fun agregarAlCarrito(carrito: Carrito): String {

        val existe = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM carrito WHERE usuario_id=? AND producto_id=?",
            Int::class.java,
            carrito.usuario_id,
            carrito.producto_id
        ) ?: 0

        return if (existe > 0) {

            jdbcTemplate.update(
                "UPDATE carrito SET cantidad = cantidad + ? WHERE usuario_id=? AND producto_id=?",
                carrito.cantidad,
                carrito.usuario_id,
                carrito.producto_id
            )

            "Cantidad actualizada"

        } else {

            jdbcTemplate.update(
                """
                INSERT INTO carrito (usuario_id, producto_id, cantidad, precio_unitario)
                VALUES (?, ?, ?, ?)
                """,
                carrito.usuario_id,
                carrito.producto_id,
                carrito.cantidad,
                carrito.precio_unitario
            )

            "Producto agregado"
        }
    }

    fun eliminarProducto(usuarioId: Int, productoId: Int): String {

        val filas = jdbcTemplate.update(
            "DELETE FROM carrito WHERE usuario_id=? AND producto_id=?",
            usuarioId,
            productoId
        )

        return if (filas > 0) "Eliminado" else "No encontrado"
    }
    fun confirmarPago(usuarioId: Int, metodo: String): String {

        val productos = jdbcTemplate.query(
            "SELECT * FROM carrito WHERE usuario_id=?",
            arrayOf(usuarioId)
        ) { rs, _ ->
            Carrito(
                rs.getInt("carrito_id"),
                rs.getInt("usuario_id"),
                rs.getInt("producto_id"),
                rs.getInt("cantidad"),
                rs.getDouble("precio_unitario")
            )
        }

        if (productos.isEmpty()) return "Carrito vacío"

        val total = productos.sumOf { it.precio_unitario * it.cantidad }


        jdbcTemplate.update(
            "INSERT INTO contrato (contrato_estado, contrato_valor, cliente_id) VALUES (1, ?, ?)",
            total,
            usuarioId
        )


        val contratoId = jdbcTemplate.queryForObject(
            "SELECT MAX(contrato_id) FROM contrato WHERE cliente_id=?",
            Int::class.java,
            usuarioId
        )

        if (contratoId == null) return "Error creando contrato"

        productos.forEach {
            jdbcTemplate.update(
                "INSERT INTO contrato_producto (contrato_id, producto_id) VALUES (?, ?)",
                contratoId,
                it.producto_id
            )
        }

        val fecha = java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date())

        jdbcTemplate.update(
            "INSERT INTO pago (pago_metodo, pago_fecha, contrato_id) VALUES (?, ?, ?)",
            metodo,
            fecha,
            contratoId
        )

        jdbcTemplate.update("DELETE FROM carrito WHERE usuario_id=?", usuarioId)

        return "Pago guardado correctamente"
    }
    fun obtenerHistorial(usuarioId: Int): List<Map<String, Any>> {

        val sql = """
        SELECT p.pago_metodo, p.pago_fecha, c.contrato_valor
        FROM pago p
        JOIN contrato c ON p.contrato_id = c.contrato_id
        WHERE c.cliente_id = ?
        ORDER BY p.pago_id DESC
    """

        return jdbcTemplate.queryForList(sql, usuarioId)
    }
}