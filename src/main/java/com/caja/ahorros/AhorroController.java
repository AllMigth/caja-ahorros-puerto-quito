package com.caja.ahorros;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/ahorros")
public class AhorroController {

    @Autowired
    private AhorroRepository ahorroRepository;

    @Autowired
    private SocioRepository socioRepository;

    // GET http://localhost:8080/ahorros
    // Lista todos los depósitos de todos los socios
    @GetMapping
    public List<Ahorro> listarTodos() {
        return ahorroRepository.findAll();
    }

    // GET http://localhost:8080/ahorros/5
    // Ver un depósito específico por ID
    @GetMapping("/{id}")
    public Ahorro obtenerPorId(@PathVariable Long id) {
        return ahorroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Depósito no encontrado"));
    }

    // POST http://localhost:8080/ahorros
    // Registrar un depósito nuevo
    @PostMapping
    public ResponseEntity<Ahorro> crearDeposito(@Valid @RequestBody AhorroRequestDTO request) {
        
        // 1. Validar que el socio exista
        Socio socio = socioRepository.findById(request.getSocioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Socio no encontrado"));

        // 2. Validar que el monto sea mayor a 0
        if (request.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El monto debe ser mayor a 0");
        }

        // 3. Crear el registro de ahorro
        Ahorro ahorro = new Ahorro(request.getMonto(), socio);
        Ahorro ahorroGuardado = ahorroRepository.save(ahorro);

        // 4. Actualizar el saldo del socio
        BigDecimal nuevoSaldo = socio.getSaldoAhorros().add(request.getMonto());
        socio.setSaldoAhorros(nuevoSaldo);
        socioRepository.save(socio);

        return ResponseEntity.ok(ahorroGuardado);
    }

    // GET http://localhost:8080/ahorros/socio/1
    // Ver todos los depósitos de un socio específico
    @GetMapping("/socio/{socioId}")
    public List<Ahorro> listarPorSocio(@PathVariable Long socioId) {
        if (!socioRepository.existsById(socioId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Socio no encontrado");
        }
        return ahorroRepository.findBySocioIdOrderByFechaDesc(socioId);
    }
}