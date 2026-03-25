package com.example.Almasoft2.repository

import com.example.Almasoft2.model.Servicio
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ServicioRepository : JpaRepository<Servicio, Int> {

    @Query(
        value = """
        SELECT s.* FROM SERVICIO s
        JOIN SERVICIO_PLAN sp ON s.servicio_id = sp.servicio_id
        JOIN PLAN_FUNEBRE p ON sp.plan_id = p.plan_id
        JOIN CONTRATO_PLAN cp ON p.plan_id = cp.plan_id
        JOIN CONTRATO c ON cp.contrato_id = c.contrato_id
        WHERE c.cliente_id = :clienteId
        """,
        nativeQuery = true
    )
    fun obtenerServiciosPorCliente(@Param("clienteId") clienteId: Int): List<Servicio>
}