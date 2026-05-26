package com.caja.ahorros;

import java.math.BigDecimal;

public class SimulacionDTO {
    private BigDecimal monto;
    private BigDecimal tasaMensual;
    private Integer plazoMeses;
    private String sistema; // "ALEMAN" o "FRANCES"

    public SimulacionDTO() {}

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public BigDecimal getTasaMensual() { return tasaMensual; }
    public void setTasaMensual(BigDecimal tasaMensual) { this.tasaMensual = tasaMensual; }

    public Integer getPlazoMeses() { return plazoMeses; }
    public void setPlazoMeses(Integer plazoMeses) { this.plazoMeses = plazoMeses; }

    public String getSistema() { return sistema; }
    public void setSistema(String sistema) { this.sistema = sistema; }
}