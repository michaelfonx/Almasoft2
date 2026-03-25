package com.example.cronograma.controller

import com.example.cronograma.model.Plan
import com.example.cronograma.service.PlanService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/planes")
class PlanController(
    private val planService: PlanService
) {

    @GetMapping
    fun obtenerPlanes(): List<Plan> {
        return planService.obtenerPlanes()
    }

    @GetMapping("/{id}")
    fun obtenerPlanPorId(@PathVariable id: Int): Plan? {
        return planService.obtenerPlanPorId(id)
    }

    @PostMapping
    fun crearPlan(@RequestBody plan: Plan): String {
        return planService.crearPlan(plan)
    }

    @PutMapping("/{id}")
    fun actualizarPlan(
        @PathVariable id: Int,
        @RequestBody plan: Plan
    ): String {
        return planService.actualizarPlan(id, plan)
    }

    @DeleteMapping("/{id}")
    fun eliminarPlan(@PathVariable id: Int): String {
        return planService.eliminarPlan(id)
    }
}