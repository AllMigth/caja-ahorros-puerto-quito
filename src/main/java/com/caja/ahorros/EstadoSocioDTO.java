package com.caja.ahorros;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class EstadoSocioDTO {
    private String nombreCompleto;
    private String cedula;
    private String telefono;
    private BigDecimal saldoAhorros;
    private PrestamoActivoDTO prestamoActivo;
    private List<MovimientoDTO> ultimosMovimientos;
    
    @Data
    public static class PrestamoActivoDTO {
        private BigDecimal montoAprobado;
        private BigDecimal saldoPendiente;
        private Integer cuotasPagadas;
        private Integer cuotasTotales;
        private BigDecimal valorCuotaActual;
        private String fechaProximoPago;
        private String sistemaAmortizacion;
        private Double tasaInteresMensual;
    }
    
    @Data
    public static class MovimientoDTO {
        private String fecha;
        private String tipo;
        private BigDecimal monto;
    }
}
