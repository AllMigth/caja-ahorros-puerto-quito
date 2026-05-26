package com.caja.ahorros;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private MovimientoAhorroRepository movimientoAhorroRepository;
    
    @Autowired
    private PagoPrestamoRepository pagoPrestamoRepository;
    
    @Autowired
    private SocioRepository socioRepository;
    
    @Autowired
    private PrestamoRepository prestamoRepository;

    // Opción 1: Corte contable al día 10
    @GetMapping("/mensual/{anio}/{mes}/desglose/corte")
    public List<ReporteSocioDTO> reporteCorteDesglose(@PathVariable int anio, @PathVariable int mes) {
        
        LocalDate fechaCorte = LocalDate.of(anio, mes, 10);
        LocalDateTime fechaHoraCorte = fechaCorte.atTime(LocalTime.MAX);
        
        List<Socio> socios = socioRepository.findAll();
        List<ReporteSocioDTO> reportes = new ArrayList<>();
        
        for (Socio socio : socios) {
            ReporteSocioDTO dto = new ReporteSocioDTO();
            dto.setSocioId(socio.getId());
            dto.setNombres(socio.getNombres());
            dto.setApellidos(socio.getApellidos());
            dto.setCedula(socio.getCedula());
            dto.setTipoReporte("CORTE");
            
            // Solo movimientos hasta el día 10
            BigDecimal totalAhorrado = movimientoAhorroRepository
                .sumDepositosPorSocioHastaFecha(socio.getId(), fechaHoraCorte);
            
            BigDecimal totalPagado = pagoPrestamoRepository
                .sumPagosPorSocioHastaFecha(socio.getId(), fechaHoraCorte);
            
            // Saldo pendiente siempre es actual
            BigDecimal saldoPendiente = prestamoRepository
                .sumSaldoPendientePorSocio(socio.getId());
                
            Integer prestamosActivos = prestamoRepository
                .countPrestamosActivosPorSocio(socio.getId());
            
            dto.setTotalAhorrado(totalAhorrado);
            dto.setTotalPagadoPrestamos(totalPagado);
            dto.setSaldoPendientePrestamos(saldoPendiente);
            dto.setCantidadPrestamosActivos(prestamosActivos);
            
            reportes.add(dto);
        }
        
        return reportes;
    }
    
    // Opción 2: Estado actual + movimientos del mes
    @GetMapping("/mensual/{anio}/{mes}/desglose/actual")
    public List<ReporteSocioDTO> reporteActualDesglose(@PathVariable int anio, @PathVariable int mes) {
        
        List<Socio> socios = socioRepository.findAll();
        List<ReporteSocioDTO> reportes = new ArrayList<>();
        
        for (Socio socio : socios) {
            ReporteSocioDTO dto = new ReporteSocioDTO();
            dto.setSocioId(socio.getId());
            dto.setNombres(socio.getNombres());
            dto.setApellidos(socio.getApellidos());
            dto.setCedula(socio.getCedula());
            dto.setTipoReporte("ACTUAL");
            
            // Saldo actual de ahorros
            BigDecimal saldoActual = movimientoAhorroRepository
                .getSaldoActualPorSocio(socio.getId());
            
            // Solo pagos del mes consultado
            BigDecimal pagosDelMes = pagoPrestamoRepository
                .sumPagosPorSocioDelMes(socio.getId(), anio, mes);
            
            // Saldo pendiente actual
            BigDecimal saldoPendiente = prestamoRepository
                .sumSaldoPendientePorSocio(socio.getId());
                
            Integer prestamosActivos = prestamoRepository
                .countPrestamosActivosPorSocio(socio.getId());
            
            dto.setTotalAhorrado(saldoActual);
            dto.setTotalPagadoPrestamos(pagosDelMes);
            dto.setSaldoPendientePrestamos(saldoPendiente);
            dto.setCantidadPrestamosActivos(prestamosActivos);
            
            reportes.add(dto);
        }
        
        return reportes;
    }
    
    // Endpoint unificado con parámetro ?tipo=corte o ?tipo=actual
    @GetMapping("/mensual/{anio}/{mes}/desglose")
    public List<ReporteSocioDTO> reporteMensualDesglose(
            @PathVariable int anio, 
            @PathVariable int mes,
            @RequestParam(defaultValue = "actual") String tipo) {
        
        if ("corte".equalsIgnoreCase(tipo)) {
            return reporteCorteDesglose(anio, mes);
        }
        return reporteActualDesglose(anio, mes);
    }
}