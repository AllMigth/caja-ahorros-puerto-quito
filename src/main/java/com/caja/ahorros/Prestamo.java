package com.caja.ahorros;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List; 

@Entity
@Table(name = "prestamo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoSolicitado;

    @Column(precision = 10, scale = 2)
    private BigDecimal montoAprobado;

    @Column(precision = 10, scale = 2)
    private BigDecimal saldoPendiente;

    // ESTE CAMPO TE FALTA - Error 1
    @Column(nullable = false)
    private Double tasaInteresMensual = 1.5;

    @Column(nullable = false)
    private Integer plazoMeses;

    // ESTE CAMPO TE FALTA - Error 2
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPrestamo estado = EstadoPrestamo.SOLICITADO;

    @Column(nullable = false)
    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SistemaAmortizacion sistemaAmortizacion = SistemaAmortizacion.FRANCES;

    // ESTE CAMPO TE FALTA - Error 3
    private LocalDateTime fechaAprobacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "socio_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Socio socio;

    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"prestamo"})
    private List<CuotaPrestamo> cuotas = new ArrayList<>();

    public List<CuotaPrestamo> getCuotas() { return cuotas; }
    public void setCuotas(List<CuotaPrestamo> cuotas) { this.cuotas = cuotas; }

    public enum SistemaAmortizacion {
        FRANCES,  // Cuota fija
        ALEMAN    // Capital fijo, cuota decreciente
    }
    public Prestamo() {}

    // GETTERS Y SETTERS - TODOS ESTOS TE FALTAN
    public SistemaAmortizacion getSistemaAmortizacion() { return sistemaAmortizacion; }
    public void setSistemaAmortizacion(SistemaAmortizacion sistemaAmortizacion) { 
        this.sistemaAmortizacion = sistemaAmortizacion; 
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getMontoSolicitado() { return montoSolicitado; }
    public void setMontoSolicitado(BigDecimal montoSolicitado) { this.montoSolicitado = montoSolicitado; }

    public BigDecimal getMontoAprobado() { return montoAprobado; }
    public void setMontoAprobado(BigDecimal montoAprobado) { this.montoAprobado = montoAprobado; }

    public BigDecimal getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(BigDecimal saldoPendiente) { this.saldoPendiente = saldoPendiente; }

    public Double getTasaInteresMensual() { return tasaInteresMensual; }
    public void setTasaInteresMensual(Double tasaInteresMensual) { this.tasaInteresMensual = tasaInteresMensual; }

    public Integer getPlazoMeses() { return plazoMeses; }
    public void setPlazoMeses(Integer plazoMeses) { this.plazoMeses = plazoMeses; }

    public EstadoPrestamo getEstado() { return estado; }
    public void setEstado(EstadoPrestamo estado) { this.estado = estado; }

    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public LocalDateTime getFechaAprobacion() { return fechaAprobacion; }
    public void setFechaAprobacion(LocalDateTime fechaAprobacion) { this.fechaAprobacion = fechaAprobacion; }

    public Socio getSocio() { return socio; }
    public void setSocio(Socio socio) { this.socio = socio; }

    // ENUM DENTRO DE LA CLASE
    public enum EstadoPrestamo {
        SOLICITADO,
        APROBADO,
        RECHAZADO,
        PAGADO,
        EN_MORA
    }
}