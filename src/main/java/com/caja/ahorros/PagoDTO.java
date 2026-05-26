package com.caja.ahorros;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PagoDTO {
    @NotNull
    @DecimalMin(value = "1.0")
    private BigDecimal montoPagado;
    
    private Boolean abonarExcedenteAAhorros = false;

    public BigDecimal getMontoPagado() { return montoPagado; }
    public void setMontoPagado(BigDecimal montoPagado) { this.montoPagado = montoPagado; }

    public Boolean getAbonarExcedenteAAhorros() { return abonarExcedenteAAhorros; }
    public void setAbonarExcedenteAAhorros(Boolean abonarExcedenteAAhorros) { 
        this.abonarExcedenteAAhorros = abonarExcedenteAAhorros; 
    }
}