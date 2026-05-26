package com.caja.ahorros;

import java.math.BigDecimal;

public class CuotaDTO {
    private Integer numero;
    private BigDecimal cuota;
    private BigDecimal interes;
    private BigDecimal amortizacion;
    private BigDecimal saldo;
    
    public CuotaDTO() {}
    
    public CuotaDTO(Integer numero, BigDecimal cuota, BigDecimal interes, 
                    BigDecimal amortizacion, BigDecimal saldo) {
        this.numero = numero;
        this.cuota = cuota;
        this.interes = interes;
        this.amortizacion = amortizacion;
        this.saldo = saldo;
    }

    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public BigDecimal getCuota() { return cuota; }
    public void setCuota(BigDecimal cuota) { this.cuota = cuota; }

    public BigDecimal getInteres() { return interes; }
    public void setInteres(BigDecimal interes) { this.interes = interes; }

    public BigDecimal getAmortizacion() { return amortizacion; }
    public void setAmortizacion(BigDecimal amortizacion) { this.amortizacion = amortizacion; }

    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
}