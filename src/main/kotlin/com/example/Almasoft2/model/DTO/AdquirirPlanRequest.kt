package com.example.Almasoft2.model.DTO

data class AdquirirPlanRequest(
    val cliente_id: Int,
    val plan_id: Int,
    val valor: Double
)