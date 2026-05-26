package com.caja.ahorros;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.caja.ahorros.Prestamo.EstadoPrestamo; // <-- IMPORTANTE
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/socios")
public class SocioController {

    @Autowired
    private SocioRepository socioRepository;
    
    @Autowired
    private AhorroRepository ahorroRepository;
    
    @Autowired
    private PrestamoRepository prestamoRepository;

    // GET /socios
    @GetMapping
    public List<Socio> listarSocios() {
        return socioRepository.findAll();
    }

    // GET /socios/1
    @GetMapping("/{id}")
    public Socio obtenerSocio(@PathVariable Long id) {
        return socioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Socio no encontrado"));
    }

    // POST /socios
    @PostMapping
    public Socio crearSocio(@RequestBody Socio socio) {
        if (socioRepository.existsByCedula(socio.getCedula())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un socio con esa cédula");
        }
        socio.setFechaIngreso(java.time.LocalDate.now());
        return socioRepository.save(socio);
    }

    // POST /socios/1/ahorros
    @PostMapping("/{id}/ahorros")
    public ResponseEntity<?> depositarAhorro(@PathVariable Long id, @RequestBody AhorroDTO ahorroDTO) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Socio no encontrado"));

        if (ahorroDTO.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto debe ser mayor a 0");
        }

        Ahorro ahorro = new Ahorro(ahorroDTO.getMonto(), socio);
        ahorroRepository.save(ahorro);

        BigDecimal nuevoSaldo = socio.getSaldoAhorros().add(ahorroDTO.getMonto());
        socio.setSaldoAhorros(nuevoSaldo);
        socioRepository.save(socio);

        return ResponseEntity.ok(socio);
    }

    // GET /socios/1/ahorros
    @GetMapping("/{id}/ahorros")
    public List<Ahorro> listarAhorrosDeSocio(@PathVariable Long id) {
        if (!socioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Socio no encontrado");
        }
        return ahorroRepository.findBySocioIdOrderByFechaDesc(id);
    }

    // POST /socios/1/prestamos - Solicitar préstamo
    @PostMapping("/{id}/prestamos")
    public ResponseEntity<?> solicitarPrestamo(@PathVariable Long id, @Valid @RequestBody PrestamoSolicitudDTO dto) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Socio no encontrado"));

        Prestamo prestamo = new Prestamo();
        prestamo.setMontoSolicitado(dto.getMontoSolicitado());
        prestamo.setTasaInteresMensual(dto.getTasaInteresMensual()); // Ojo: usa el nombre de tu campo
        prestamo.setPlazoMeses(dto.getPlazoMeses());
        prestamo.setSocio(socio);
        prestamo.setEstado(EstadoPrestamo.SOLICITADO); // <-- USA EL ENUM, NO STRING
        
        Prestamo guardado = prestamoRepository.save(prestamo);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    // GET /socios/1/prestamos - Ver préstamos de un socio
    @GetMapping("/{id}/prestamos")
    public List<Prestamo> listarPrestamosDeSocio(@PathVariable Long id) {
        if (!socioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Socio no encontrado");
        }
        return prestamoRepository.findBySocioIdOrderByFechaSolicitudDesc(id);
    }

}