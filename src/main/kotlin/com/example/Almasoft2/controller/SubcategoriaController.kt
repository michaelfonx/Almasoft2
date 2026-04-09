package com.example.cronograma.controller

import com.example.cronograma.model.Subcategoria
import com.example.cronograma.service.SubcategoriaService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/subcategorias")
class SubcategoriaController(
    private val subcategoriaService: SubcategoriaService
) {

    @GetMapping
    fun obtenerSubcategorias(): List<Subcategoria> {
        return subcategoriaService.obtenerSubcategorias()
    }

    @GetMapping("/{id}")
    fun obtenerSubcategoriaPorId(@PathVariable id: Int): Subcategoria? {
        return subcategoriaService.obtenerSubcategoriaPorId(id)
    }

    @PostMapping
    fun crearSubcategoria(@RequestBody subcategoria: Subcategoria): String {
        return subcategoriaService.crearSubcategoria(subcategoria)
    }

    @PutMapping("/{id}")
    fun actualizarSubcategoria(
        @PathVariable id: Int,
        @RequestBody subcategoria: Subcategoria
    ): String {
        return subcategoriaService.actualizarSubcategoria(id, subcategoria)
    }

    @DeleteMapping("/{id}")
    fun eliminarSubcategoria(@PathVariable id: Int): String {
        return subcategoriaService.eliminarSubcategoria(id)
    }
    @GetMapping("/categoria/{id}")
    fun obtenerPorCategoria(@PathVariable id: Int): List<Subcategoria> {
        return subcategoriaService.obtenerPorCategoria(id)
    }
}