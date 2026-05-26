package com.caja.ahorros;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cuota_prestamo")
public class CuotaPrestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer numeroCuota;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal capital;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal interes;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cuotaTotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal saldoPendiente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCuota estado = EstadoCuota.PENDIENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestamo_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "cuotas"})
    private Prestamo prestamo;

    public enum EstadoCuota {
        PENDIENTE, PAGADA, VENCIDA
    }

    public CuotaPrestamo() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getNumeroCuota() { return numeroCuota; }
    public void setNumeroCuota(Integer numeroCuota) { this.numeroCuota = numeroCuota; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public BigDecimal getCapital() { return capital; }
    public void setCapital(BigDecimal capital) { this.capital = capital; }

    public BigDecimal getInteres() { return interes; }
    public void setInteres(BigDecimal interes) { this.interes = interes; }

    public BigDecimal getCuotaTotal() { return cuotaTotal; }
    public void setCuotaTotal(BigDecimal cuotaTotal) { this.cuotaTotal = cuotaTotal; }

    public BigDecimal getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(BigDecimal saldoPendiente) { this.saldoPendiente = saldoPendiente; }

    public EstadoCuota getEstado() { return estado; }
    public void setEstado(EstadoCuota estado) { this.estado = estado; }

    public Prestamo getPrestamo() { return prestamo; }
    public void setPrestamo(Prestamo prestamo) { this.prestamo = prestamo; }
}