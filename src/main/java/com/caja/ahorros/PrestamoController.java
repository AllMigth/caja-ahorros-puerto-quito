package com.caja.ahorros;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.math.RoundingMode;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private AmortizacionService amortizacionService;

    @Autowired
    private CuotaPrestamoRepository cuotaPrestamoRepository;

    @Autowired
    private SocioRepository socioRepository;
    
    @Autowired
    private ParametroService parametroService;
    
    @Autowired
    private PagoPrestamoRepository pagoPrestamoRepository;
        
    @Autowired
    private MovimientoAhorroRepository movimientoAhorroRepository;

    // GET /prestamos - Listar todos los préstamos
    @GetMapping
    public List<Prestamo> listarTodos() {
        return prestamoRepository.findAll();
    }

    // GET /prestamos/{id} - Ver un préstamo
    @GetMapping("/{id}")
    public Prestamo obtenerPorId(@PathVariable Long id) {
        return prestamoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Préstamo no encontrado"));
    }

@PutMapping("/{id}/aprobar")
@Transactional
public Prestamo aprobarPrestamo(@PathVariable Long id, @RequestBody AprobarPrestamoDTO dto) {
    Prestamo prestamo = prestamoRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Préstamo no encontrado"));

    if (prestamo.getEstado() != Prestamo.EstadoPrestamo.SOLICITADO) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se pueden aprobar préstamos solicitados");
    }

    if (dto.getMontoAprobado().compareTo(prestamo.getMontoSolicitado()) > 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
            "No puedes aprobar más de lo solicitado: $" + prestamo.getMontoSolicitado());
    }

    BigDecimal multiploMax = parametroService.getDecimal("PRESTAMO_MAX_MULTIPLO", new BigDecimal("5"));
    BigDecimal montoMinimo = parametroService.getDecimal("PRESTAMO_MONTO_MINIMO", new BigDecimal("50"));
    
    BigDecimal saldoSocio = prestamo.getSocio().getSaldoAhorros();
    BigDecimal maximoPermitido = saldoSocio.multiply(multiploMax);
    
    if (dto.getMontoAprobado().compareTo(maximoPermitido) > 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
            "Monto excede el límite. Máximo permitido: $" + maximoPermitido);
    }

    if (dto.getMontoAprobado().compareTo(montoMinimo) < 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
            "Monto mínimo a aprobar: $" + montoMinimo);
    }
    
    Integer prestamosActivos = prestamoRepository.countPrestamosActivosPorSocio(prestamo.getSocio().getId());
    if (prestamosActivos > 0) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
            "El socio ya tiene un préstamo activo. Debe liquidarlo antes de solicitar otro.");
    }
    
    prestamo.setMontoAprobado(dto.getMontoAprobado());
    prestamo.setSaldoPendiente(dto.getMontoAprobado());
    prestamo.setEstado(Prestamo.EstadoPrestamo.APROBADO);
    prestamo.setFechaAprobacion(LocalDateTime.now());

    Prestamo prestamoGuardado = prestamoRepository.save(prestamo);
    
    // Generar tabla de amortización
    amortizacionService.generarTabla(prestamoGuardado);
    
    return prestamoGuardado;
    }

    // GET /prestamos/estado/SOLICITADO - Ver préstamos pendientes
    @GetMapping("/estado/{estado}")
    public List<Prestamo> listarPorEstado(@PathVariable Prestamo.EstadoPrestamo estado) {
        return prestamoRepository.findByEstado(estado);
    }


    @Transactional
    @PostMapping("/{id}/pagos")
    public ResponseEntity<?> registrarPago(@PathVariable Long id, @Valid @RequestBody PagoDTO dto) {
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Préstamo no encontrado"));

        if (prestamo.getEstado() != Prestamo.EstadoPrestamo.APROBADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se pagan préstamos aprobados");
        }

        if (prestamo.getSaldoPendiente().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Préstamo ya pagado");
        }

        // 1. Calcular interés del mes
        BigDecimal interes = prestamo.getSaldoPendiente()
                .multiply(BigDecimal.valueOf(prestamo.getTasaInteresMensual()))
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        
        // 2. Total que debe: interés + saldo pendiente
        BigDecimal totalAdeudado = interes.add(prestamo.getSaldoPendiente());
        
        // 3. Manejar sobrepago
        BigDecimal montoACobrar = dto.getMontoPagado();
        BigDecimal excedente = BigDecimal.ZERO;
        
        if (dto.getMontoPagado().compareTo(totalAdeudado) > 0) {
            if (Boolean.TRUE.equals(dto.getAbonarExcedenteAAhorros())) {
                // Abonar excedente a ahorros
                excedente = dto.getMontoPagado().subtract(totalAdeudado);
                montoACobrar = totalAdeudado;
                
                Socio socio = prestamo.getSocio();
                socio.setSaldoAhorros(socio.getSaldoAhorros().add(excedente));
                socioRepository.save(socio);
                
                MovimientoAhorro mov = new MovimientoAhorro();
                mov.setTipo(MovimientoAhorro.TipoMovimiento.DEPOSITO);
                mov.setMonto(excedente);
                mov.setDescripcion("Excedente de pago préstamo #" + prestamo.getId());
                mov.setSocio(socio);
                movimientoAhorroRepository.save(mov);
                
            } else {
                // Rechazar sobrepago
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "Monto excede la deuda. Total a pagar: $" + totalAdeudado + 
                    ". Use 'abonarExcedenteAAhorros: true' para abonar $" + 
                    dto.getMontoPagado().subtract(totalAdeudado) + " a ahorros.");
            }
        }
        
        BigDecimal capital = montoACobrar.subtract(interes);

        if (capital.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Monto insuficiente. Interés del mes: $" + interes);
        }

        // Registrar pago
        PagoPrestamo pago = new PagoPrestamo();
        pago.setMontoPagado(montoACobrar);
        pago.setInteresPagado(interes);
        pago.setCapitalPagado(capital);
        pago.setPrestamo(prestamo);
        pago.setNumeroCuota(pagoPrestamoRepository.countByPrestamoId(id) + 1);
        pago.setFechaPago(LocalDateTime.now());
        pagoPrestamoRepository.save(pago);

        // Actualizar saldo préstamo
        BigDecimal nuevoSaldo = prestamo.getSaldoPendiente().subtract(capital);
        prestamo.setSaldoPendiente(nuevoSaldo);
        
        if (nuevoSaldo.compareTo(new BigDecimal("0.01")) < 0) {
            prestamo.setEstado(Prestamo.EstadoPrestamo.PAGADO);
            prestamo.setSaldoPendiente(BigDecimal.ZERO);
        }
        
        prestamoRepository.save(prestamo);
        
        // Respuesta con excedente si hubo
        if (excedente.compareTo(BigDecimal.ZERO) > 0) {
            return ResponseEntity.ok(new PagoResponse(pago, excedente));
        }
        
        return ResponseEntity.ok(pago);
    }
    

    @PostMapping
    public Prestamo solicitarPrestamo(@RequestBody PrestamoDTO dto) {
    Socio socio = socioRepository.findById(dto.getSocioId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Socio no encontrado"));

    Prestamo prestamo = new Prestamo();
    prestamo.setSocio(socio);
    prestamo.setMontoSolicitado(dto.getMontoSolicitado());
    prestamo.setTasaInteresMensual(dto.getTasaInteresMensual());
    prestamo.setPlazoMeses(dto.getPlazoMeses());
    prestamo.setSistemaAmortizacion(dto.getSistemaAmortizacion()); // <-- AGREGA ESTO
    prestamo.setEstado(Prestamo.EstadoPrestamo.SOLICITADO);

    return prestamoRepository.save(prestamo);
}

    @GetMapping("/{id}/pagos")
    public List<PagoPrestamo> listarPagos(@PathVariable Long id) {
        return pagoPrestamoRepository.findByPrestamoIdOrderByNumeroCuotaAsc(id);
    }

    @GetMapping("/{id}/tabla-amortizacion")
    public List<CuotaPrestamo> verTablaAmortizacion(@PathVariable Long id) {
        if (!prestamoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Préstamo no encontrado");
        }
        return cuotaPrestamoRepository.findByPrestamoIdOrderByNumeroCuotaAsc(id);
    }
}