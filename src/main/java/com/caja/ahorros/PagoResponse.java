package com.caja.ahorros;

import java.math.BigDecimal;

public class PagoResponse {
    private PagoPrestamo pago;
    private BigDecimal excedenteAbonadoAAhorros;
    private String mensaje;

    public PagoResponse(PagoPrestamo pago, BigDecimal excedente) {
        this.pago = pago;
        this.excedenteAbonadoAAhorros = excedente;
        this.mensaje = "Se abonó $" + excedente + " a su cuenta de ahorros";
    }

    public PagoPrestamo getPago() { return pago; }
    public BigDecimal getExcedenteAbonadoAAhorros() { return excedenteAbonadoAAhorros; }
    public String getMensaje() { return mensaje; }
}