package com.caja.ahorros;

import java.math.BigDecimal;

public class ReporteSocioDTO {
    private Long socioId;
    private String nombres;
    private String apellidos;
    private String cedula;
    private BigDecimal totalAhorrado;
    private BigDecimal totalPagadoPrestamos;
    private BigDecimal saldoPendientePrestamos;
    private Integer cantidadPrestamosActivos;
    private String tipoReporte;
    
    public ReporteSocioDTO() {}

    // Getters y Setters

    public String getTipoReporte() { return tipoReporte; }
    public void setTipoReporte(String tipoReporte) { this.tipoReporte = tipoReporte; }

    public Long getSocioId() { return socioId; }
    public void setSocioId(Long socioId) { this.socioId = socioId; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public BigDecimal getTotalAhorrado() { return totalAhorrado; }
    public void setTotalAhorrado(BigDecimal totalAhorrado) { this.totalAhorrado = totalAhorrado; }

    public BigDecimal getTotalPagadoPrestamos() { return totalPagadoPrestamos; }
    public void setTotalPagadoPrestamos(BigDecimal totalPagadoPrestamos) { 
        this.totalPagadoPrestamos = totalPagadoPrestamos; 
    }

    public BigDecimal getSaldoPendientePrestamos() { return saldoPendientePrestamos; }
    public void setSaldoPendientePrestamos(BigDecimal saldoPendientePrestamos) { 
        this.saldoPendientePrestamos = saldoPendientePrestamos; 
    }

    public Integer getCantidadPrestamosActivos() { return cantidadPrestamosActivos; }
    public void setCantidadPrestamosActivos(Integer cantidadPrestamosActivos) { 
        this.cantidadPrestamosActivos = cantidadPrestamosActivos; 
    }
}