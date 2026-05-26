package com.caja.ahorros;

import java.math.BigDecimal;

public class PrestamoDTO {
    private Long socioId;
    private BigDecimal montoSolicitado;
    private Double tasaInteresMensual;
    private Integer plazoMeses;
    private Prestamo.SistemaAmortizacion sistemaAmortizacion = Prestamo.SistemaAmortizacion.FRANCES;

    // Getters y Setters
    public Long getSocioId() { return socioId; }
    public void setSocioId(Long socioId) { this.socioId = socioId; }
    public BigDecimal getMontoSolicitado() { return montoSolicitado; }
    public void setMontoSolicitado(BigDecimal montoSolicitado) { this.montoSolicitado = montoSolicitado; }
    public Double getTasaInteresMensual() { return tasaInteresMensual; }
    public void setTasaInteresMensual(Double tasaInteresMensual) { this.tasaInteresMensual = tasaInteresMensual; }
    public Integer getPlazoMeses() { return plazoMeses; }
    public void setPlazoMeses(Integer plazoMeses) { this.plazoMeses = plazoMeses; }
    public Prestamo.SistemaAmortizacion getSistemaAmortizacion() { return sistemaAmortizacion; }
    public void setSistemaAmortizacion(Prestamo.SistemaAmortizacion sistemaAmortizacion) { 
        this.sistemaAmortizacion = sistemaAmortizacion; 
    }
}