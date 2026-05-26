package com.caja.ahorros;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago_prestamo")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PagoPrestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montoPagado;

    @Column(nullable = false)
    private LocalDateTime fechaPago = LocalDateTime.now();

    @Column(nullable = false)
    private Integer numeroCuota;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal interesPagado;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal capitalPagado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestamo_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Prestamo prestamo;

    public PagoPrestamo() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getMontoPagado() { return montoPagado; }
    public void setMontoPagado(BigDecimal montoPagado) { this.montoPagado = montoPagado; }

    public LocalDateTime getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDateTime fechaPago) { this.fechaPago = fechaPago; }

    public Integer getNumeroCuota() { return numeroCuota; }
    public void setNumeroCuota(Integer numeroCuota) { this.numeroCuota = numeroCuota; }

    public BigDecimal getInteresPagado() { return interesPagado; }
    public void setInteresPagado(BigDecimal interesPagado) { this.interesPagado = interesPagado; }

    public BigDecimal getCapitalPagado() { return capitalPagado; }
    public void setCapitalPagado(BigDecimal capitalPagado) { this.capitalPagado = capitalPagado; }

    public Prestamo getPrestamo() { return prestamo; }
    public void setPrestamo(Prestamo prestamo) { this.prestamo = prestamo; }
}