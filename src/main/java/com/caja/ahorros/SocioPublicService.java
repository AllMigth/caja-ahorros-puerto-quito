package com.caja.ahorros;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocioPublicService {

    @Autowired
    private SocioRepository socioRepository;
    
    @Autowired
    private PrestamoRepository prestamoRepository;
    
    @Autowired
    private MovimientoAhorroRepository movimientoRepository;

    public EstadoSocioDTO obtenerEstadoPorCedula(String cedula) {
        Socio socio = socioRepository.findByCedula(cedula)
            .orElseThrow(() -> new RuntimeException("Socio no encontrado"));
        
        EstadoSocioDTO dto = new EstadoSocioDTO();
        dto.setNombreCompleto(socio.getNombres() + " " + socio.getApellidos());
        dto.setCedula(socio.getCedula());
        dto.setSaldoAhorros(socio.getSaldoAhorros());
        
        // Busca préstamo activo: APROBADO
        prestamoRepository.findBySocioAndEstado(socio, Prestamo.EstadoPrestamo.APROBADO)
            .ifPresent(prestamo -> {
                EstadoSocioDTO.PrestamoActivoDTO p = new EstadoSocioDTO.PrestamoActivoDTO();
                p.setMontoAprobado(prestamo.getMontoAprobado());
                p.setSaldoPendiente(prestamo.getSaldoPendiente());
                p.setCuotasTotales(prestamo.getPlazoMeses());
                p.setSistemaAmortizacion(prestamo.getSistemaAmortizacion().toString());
                
                // Cuotas pagadas
                long pagadas = prestamo.getCuotas().stream()
                    .filter(c -> c.getEstado() == CuotaPrestamo.EstadoCuota.PAGADA)
                    .count();
                p.setCuotasPagadas((int) pagadas);
                
                // Próxima cuota pendiente
                prestamo.getCuotas().stream()
                    .filter(c -> c.getEstado() == CuotaPrestamo.EstadoCuota.PENDIENTE)
                    .findFirst()
                    .ifPresent(cuota -> {
                        p.setValorCuotaActual(cuota.getCuotaTotal());
                        p.setFechaProximoPago(cuota.getFechaVencimiento()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                    });
                
                dto.setPrestamoActivo(p);
            });
        
        // Últimos 5 movimientos
        List<EstadoSocioDTO.MovimientoDTO> movs = movimientoRepository
            .findTop5BySocioOrderByFechaDesc(socio)
            .stream()
            .map(m -> {
                EstadoSocioDTO.MovimientoDTO mov = new EstadoSocioDTO.MovimientoDTO();
                mov.setFecha(m.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                mov.setTipo(m.getTipo().toString());
                mov.setMonto(m.getMonto());
                return mov;
            })
            .collect(Collectors.toList());
        dto.setUltimosMovimientos(movs);
        
        return dto;
    }
    
    public List<Socio> buscarSociosPorNombre(String nombre) {
        return socioRepository.buscarPorNombre(nombre);
    }
}
