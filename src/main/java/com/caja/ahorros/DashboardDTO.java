package com.caja.ahorros;

import java.math.BigDecimal;

public class DashboardDTO {
    // Totales
    private BigDecimal totalAhorros;
    private BigDecimal carteraActiva;
    private BigDecimal cobradoMesActual;
    private BigDecimal interesesGeneradosMes;
    
    // Conteos
    private Integer totalSocios;
    private Integer sociosActivos;
    private Integer prestamosActivos;
    private Integer prestamosAprobadosMes;
    private Integer prestamosPorVencer; // Próximos 7 días
    
    // Indicadores
    private BigDecimal porcentajeMorosidad;
    private BigDecimal promedioAhorroPorSocio;

    public DashboardDTO() {}

    // Getters y Setters
    public BigDecimal getTotalAhorros() { return totalAhorros; }
    public void setTotalAhorros(BigDecimal totalAhorros) { this.totalAhorros = totalAhorros; }

    public BigDecimal getCarteraActiva() { return carteraActiva; }
    public void setCarteraActiva(BigDecimal carteraActiva) { this.carteraActiva = carteraActiva; }

    public BigDecimal getCobradoMesActual() { return cobradoMesActual; }
    public void setCobradoMesActual(BigDecimal cobradoMesActual) { this.cobradoMesActual = cobradoMesActual; }

    public BigDecimal getInteresesGeneradosMes() { return interesesGeneradosMes; }
    public void setInteresesGeneradosMes(BigDecimal interesesGeneradosMes) { 
        this.interesesGeneradosMes = interesesGeneradosMes; 
    }

    public Integer getTotalSocios() { return totalSocios; }
    public void setTotalSocios(Integer totalSocios) { this.totalSocios = totalSocios; }

    public Integer getSociosActivos() { return sociosActivos; }
    public void setSociosActivos(Integer sociosActivos) { this.sociosActivos = sociosActivos; }

    public Integer getPrestamosActivos() { return prestamosActivos; }
    public void setPrestamosActivos(Integer prestamosActivos) { this.prestamosActivos = prestamosActivos; }

    public Integer getPrestamosAprobadosMes() { return prestamosAprobadosMes; }
    public void setPrestamosAprobadosMes(Integer prestamosAprobadosMes) { 
        this.prestamosAprobadosMes = prestamosAprobadosMes; 
    }

    public Integer getPrestamosPorVencer() { return prestamosPorVencer; }
    public void setPrestamosPorVencer(Integer prestamosPorVencer) { 
        this.prestamosPorVencer = prestamosPorVencer; 
    }

    public BigDecimal getPorcentajeMorosidad() { return porcentajeMorosidad; }
    public void setPorcentajeMorosidad(BigDecimal porcentajeMorosidad) { 
        this.porcentajeMorosidad = porcentajeMorosidad; 
    }

    public BigDecimal getPromedioAhorroPorSocio() { return promedioAhorroPorSocio; }
    public void setPromedioAhorroPorSocio(BigDecimal promedioAhorroPorSocio) { 
        this.promedioAhorroPorSocio = promedioAhorroPorSocio; 
    }
}