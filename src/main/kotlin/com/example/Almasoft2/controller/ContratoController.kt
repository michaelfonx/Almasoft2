package com.example.cronograma.controller

import com.example.Almasoft2.model.DTO.AdquirirPlanRequest
import com.example.cronograma.model.Contrato
import com.example.cronograma.model.DTO.MiPlanDTO
import com.example.cronograma.service.ContratoService
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.http.MediaType

@RestController
@RequestMapping("/api")
class ContratoController(
    private val contratoService: ContratoService
) {

    @GetMapping("/contratos")
    fun verContratos(): List<Contrato> {
        return contratoService.obtenerContratos()
    }

    @GetMapping("/contratos/{id}")
    fun verContratoPorId(@PathVariable id: Int): Contrato? {
        return contratoService.obtenerContratoPorId(id)
    }

    @PostMapping("/contratos")
    fun crearContrato(@RequestBody contrato: Contrato): Int {
        return contratoService.crearContrato(contrato)
    }

    @PutMapping("/contratos/{id}")
    fun actualizarContrato(
        @PathVariable id: Int,
        @RequestBody contrato: Contrato
    ): String {
        return contratoService.actualizarContrato(id, contrato)
    }

    @DeleteMapping("/contratos/{id}")
    fun eliminarContrato(@PathVariable id: Int): String {
        return contratoService.eliminarContrato(id)
    }


    @GetMapping("/mi-plan/{clienteId}")
    fun obtenerMiPlan(@PathVariable clienteId: Int): MiPlanDTO? {
        return contratoService.obtenerMiPlan(clienteId)
    }
    @PostMapping("/adquirir-plan")
    fun adquirirPlan(@RequestBody request: AdquirirPlanRequest): Map<String, Int> {

        val contratoId = contratoService.adquirirPlan(
            request.cliente_id,
            request.plan_id,
            request.valor
        )

        return mapOf("contrato_id" to contratoId)
    }
    @PostMapping("/afiliado")
    fun agregarAfiliado(@RequestBody body: Map<String, Int>): String {

        val contratoId = body["contrato_id"] ?: 0
        val usuarioId = body["usuario_id"] ?: 0

        return contratoService.agregarAfiliado(contratoId, usuarioId)
    }
    @GetMapping("/afiliados/{contratoId}")
    fun obtenerAfiliados(@PathVariable contratoId: Int) =
        contratoService.obtenerAfiliados(contratoId)

    @PostMapping("/afiliado-documento")
    fun agregarAfiliadoPorDocumento(@RequestBody body: Map<String, Int>): String {

        val contratoId = body["contrato_id"] ?: return "Contrato inválido"
        val documento = body["documento"] ?: return "Documento inválido"

        return contratoService.agregarAfiliadoPorDocumento(contratoId, documento)
    }
    @PostMapping("/contrato-producto", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun agregarProductoContrato(@RequestBody body: Map<String, Int>): ResponseEntity<Map<String, String>> {

        val contratoId = body["contrato_id"] ?: return ResponseEntity.badRequest()
            .body(mapOf("error" to "Contrato inválido"))

        val productoId = body["producto_id"] ?: return ResponseEntity.badRequest()
            .body(mapOf("error" to "Producto inválido"))

        val resultado = contratoService.agregarProductoAContrato(contratoId, productoId)

        return ResponseEntity.ok(mapOf("mensaje" to resultado))
    }
}