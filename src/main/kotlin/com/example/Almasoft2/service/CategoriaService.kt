package com.example.cronograma.service

import com.example.cronograma.model.Categoria
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class CategoriaService(
    private val jdbcTemplate: JdbcTemplate
) {

    fun obtenerCategorias(): List<Categoria> {

        val sql = "SELECT * FROM categoria"

        return jdbcTemplate.query(sql) { rs, _ ->
            Categoria(
                rs.getInt("categoria_id"),
                rs.getString("categoria_nombre")
            )
        }
    }

    fun obtenerCategoriaPorId(id: Int): Categoria? {

        val sql = "SELECT * FROM categoria WHERE categoria_id = ?"

        return jdbcTemplate.query(sql, arrayOf(id)) { rs, _ ->
            Categoria(
                rs.getInt("categoria_id"),
                rs.getString("categoria_nombre")
            )
        }.firstOrNull()
    }

    fun crearCategoria(categoria: Categoria): String {

        val sql = "INSERT INTO categoria (categoria_nombre) VALUES (?)"

        jdbcTemplate.update(sql, categoria.categoria_nombre)

        return "Categoria creada"
    }

    fun actualizarCategoria(id: Int, categoria: Categoria): String {

        val sql = "UPDATE categoria SET categoria_nombre = ? WHERE categoria_id = ?"

        val filas = jdbcTemplate.update(sql, categoria.categoria_nombre, id)

        return if (filas > 0) "Categoria actualizada"
        else "Categoria no encontrada"
    }

    fun eliminarCategoria(id: Int): String {

        val sql = "DELETE FROM categoria WHERE categoria_id = ?"

        val filas = jdbcTemplate.update(sql, id)

        return if (filas > 0) "Categoria eliminada"
        else "Categoria no encontrada"
    }
}