package com.caja.ahorros;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReporteDTO {
    private LocalDate fechaCorte;
    private BigDecimal totalAhorrosDepositados;
    private BigDecimal totalPagosPrestamos;
    private BigDecimal totalGeneral;
    private Integer cantidadSocios;
    private Integer cantidadPrestamosActivos;

    public ReporteDTO() {}

    // Getters y Setters
    public LocalDate getFechaCorte() { return fechaCorte; }
    public void setFechaCorte(LocalDate fechaCorte) { this.fechaCorte = fechaCorte; }

    public BigDecimal getTotalAhorrosDepositados() { return totalAhorrosDepositados; }
    public void setTotalAhorrosDepositados(BigDecimal totalAhorrosDepositados) { 
        this.totalAhorrosDepositados = totalAhorrosDepositados; 
    }

    public BigDecimal getTotalPagosPrestamos() { return totalPagosPrestamos; }
    public void setTotalPagosPrestamos(BigDecimal totalPagosPrestamos) { 
        this.totalPagosPrestamos = totalPagosPrestamos; 
    }

    public BigDecimal getTotalGeneral() { return totalGeneral; }
    public void setTotalGeneral(BigDecimal totalGeneral) { this.totalGeneral = totalGeneral; }

    public Integer getCantidadSocios() { return cantidadSocios; }
    public void setCantidadSocios(Integer cantidadSocios) { this.cantidadSocios = cantidadSocios; }

    public Integer getCantidadPrestamosActivos() { return cantidadPrestamosActivos; }
    public void setCantidadPrestamosActivos(Integer cantidadPrestamosActivos) { 
        this.cantidadPrestamosActivos = cantidadPrestamosActivos; 
    }
}