package com.example.cronograma.service

import com.example.cronograma.model.Producto
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class ProductoService(
    private val jdbcTemplate: JdbcTemplate
) {

    // 🔹 GET TODOS
    fun obtenerProductos(): List<Producto> {

        val sql = "SELECT * FROM producto"

        return jdbcTemplate.query(sql) { rs, _ ->
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
    }

    // 🔹 GET POR ID
    fun obtenerProductoPorId(id: Int): Producto? {

        val sql = "SELECT * FROM producto WHERE producto_id = ?"

        return jdbcTemplate.query(sql, arrayOf(id)) { rs, _ ->
            Producto(
                rs.getInt("producto_id"),
                rs.getString("producto_nombre"),
                rs.getString("producto_descripcion"),
                rs.getDouble("producto_precio"),
                rs.getInt("producto_stock"),
                rs.getBoolean("producto_estado"),
                rs.getInt("subcategoria_id")
            )
        }.firstOrNull()
    }

    // 🔥 POST
    fun crearProducto(producto: Producto): String {

        val sql = """
            INSERT INTO producto (
                producto_nombre,
                producto_descripcion,
                producto_precio,
                producto_stock,
                producto_estado,
                subcategoria_id
            )
            VALUES (?, ?, ?, ?, ?, ?)
        """

        jdbcTemplate.update(
            sql,
            producto.producto_nombre,
            producto.producto_descripcion,
            producto.producto_precio,
            producto.producto_stock,
            producto.producto_estado,
            producto.subcategoria_id
        )

        return "Producto creado correctamente"
    }

    // 🔹 PUT
    fun actualizarProducto(id: Int, producto: Producto): String {

        val sql = """
            UPDATE producto
            SET producto_nombre = ?,
                producto_descripcion = ?,
                producto_precio = ?,
                producto_stock = ?,
                producto_estado = ?,
                subcategoria_id = ?
            WHERE producto_id = ?
        """

        val filas = jdbcTemplate.update(
            sql,
            producto.producto_nombre,
            producto.producto_descripcion,
            producto.producto_precio,
            producto.producto_stock,
            producto.producto_estado,
            producto.subcategoria_id,
            id
        )

        return if (filas > 0) "Producto actualizado"
        else "Producto no encontrado"
    }

    // 🔹 DELETE
    fun eliminarProducto(id: Int): String {

        val sql = "DELETE FROM producto WHERE producto_id = ?"

        val filas = jdbcTemplate.update(sql, id)

        return if (filas > 0) "Producto eliminado"
        else "Producto no encontrado"
    }
}