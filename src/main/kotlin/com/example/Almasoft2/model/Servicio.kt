package com.example.Almasoft2.model

import jakarta.persistence.*

@Entity
@Table(name = "servicio")
data class Servicio(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "servicio_id")
    var servicioId: Long? = null,

    @Column(name = "servicio_nombre")
    var servicioNombre: String? = null,

    @Column(name = "servicio_descripcion")
    var servicioDescripcion: String? = null,

    @Column(name = "servicio_precio")
    var servicioPrecio: Double? = null
)