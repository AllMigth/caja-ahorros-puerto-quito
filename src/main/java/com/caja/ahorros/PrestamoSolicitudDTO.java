package com.caja.ahorros;

import java.math.BigDecimal;

public class PrestamoSolicitudDTO {
    private BigDecimal montoSolicitado;
    private Double tasaInteresMensual;
    private Integer plazoMeses;

    public BigDecimal getMontoSolicitado() { return montoSolicitado; }
    public void setMontoSolicitado(BigDecimal montoSolicitado) { this.montoSolicitado = montoSolicitado; }
    public Double getTasaInteresMensual() { return tasaInteresMensual; }
    public void setTasaInteresMensual(Double tasaInteresMensual) { this.tasaInteresMensual = tasaInteresMensual; }
    public Integer getPlazoMeses() { return plazoMeses; }
    public void setPlazoMeses(Integer plazoMeses) { this.plazoMeses = plazoMeses; }
}