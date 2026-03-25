package com.example.cronograma.controller

import com.example.cronograma.model.Categoria
import com.example.cronograma.service.CategoriaService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/categorias")
class CategoriaController(
    private val categoriaService: CategoriaService
) {

    @GetMapping
    fun obtenerCategorias(): List<Categoria> {
        return categoriaService.obtenerCategorias()
    }

    @GetMapping("/{id}")
    fun obtenerCategoriaPorId(@PathVariable id: Int): Categoria? {
        return categoriaService.obtenerCategoriaPorId(id)
    }

    @PostMapping
    fun crearCategoria(@RequestBody categoria: Categoria): String {
        return categoriaService.crearCategoria(categoria)
    }

    @PutMapping("/{id}")
    fun actualizarCategoria(
        @PathVariable id: Int,
        @RequestBody categoria: Categoria
    ): String {
        return categoriaService.actualizarCategoria(id, categoria)
    }

    @DeleteMapping("/{id}")
    fun eliminarCategoria(@PathVariable id: Int): String {
        return categoriaService.eliminarCategoria(id)
    }
}