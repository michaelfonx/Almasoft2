package com.example.cronograma.service

import com.example.cronograma.model.Subcategoria
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service

@Service
class SubcategoriaService(
    private val jdbcTemplate: JdbcTemplate
) {

    fun obtenerSubcategorias(): List<Subcategoria> {

        val sql = "SELECT * FROM subcategoria"

        return jdbcTemplate.query(sql) { rs, _ ->
            Subcategoria(
                rs.getInt("subcategoria_id"),
                rs.getString("subcategoria_nombre"),
                rs.getInt("categoria_id")
            )
        }
    }

    fun obtenerSubcategoriaPorId(id: Int): Subcategoria? {

        val sql = "SELECT * FROM subcategoria WHERE subcategoria_id = ?"

        return jdbcTemplate.query(sql, arrayOf(id)) { rs, _ ->
            Subcategoria(
                rs.getInt("subcategoria_id"),
                rs.getString("subcategoria_nombre"),
                rs.getInt("categoria_id")
            )
        }.firstOrNull()
    }

    fun crearSubcategoria(subcategoria: Subcategoria): String {

        val sql = """
            INSERT INTO subcategoria (subcategoria_nombre, categoria_id)
            VALUES (?, ?)
        """

        jdbcTemplate.update(
            sql,
            subcategoria.subcategoria_nombre,
            subcategoria.categoria_id
        )

        return "Subcategoria creada"
    }

    fun actualizarSubcategoria(id: Int, subcategoria: Subcategoria): String {

        val sql = """
            UPDATE subcategoria
            SET subcategoria_nombre = ?, categoria_id = ?
            WHERE subcategoria_id = ?
        """

        val filas = jdbcTemplate.update(
            sql,
            subcategoria.subcategoria_nombre,
            subcategoria.categoria_id,
            id
        )

        return if (filas > 0) "Subcategoria actualizada"
        else "Subcategoria no encontrada"
    }

    fun eliminarSubcategoria(id: Int): String {

        val sql = "DELETE FROM subcategoria WHERE subcategoria_id = ?"

        val filas = jdbcTemplate.update(sql, id)

        return if (filas > 0) "Subcategoria eliminada"
        else "Subcategoria no encontrada"
    }
    fun obtenerPorCategoria(categoriaId: Int): List<Subcategoria> {

        val sql = "SELECT * FROM subcategoria WHERE categoria_id = ?"

        return jdbcTemplate.query(sql, arrayOf(categoriaId)) { rs, _ ->
            Subcategoria(
                rs.getInt("subcategoria_id"),
                rs.getString("subcategoria_nombre"),
                rs.getInt("categoria_id")
            )
        }
    }
}