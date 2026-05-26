package com.caja.ahorros;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Long> {
    
    // Busca un socio por cédula. Spring crea el SQL solo
    Optional<Socio> findByCedula(String cedula);
    
    // Verifica si ya existe esa cédula
    boolean existsByCedula(String cedula);
}