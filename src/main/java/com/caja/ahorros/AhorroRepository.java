package com.caja.ahorros;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AhorroRepository extends JpaRepository<Ahorro, Long> {
    
    // Trae todos los ahorros de un socio ordenados por fecha
    List<Ahorro> findBySocioIdOrderByFechaDesc(Long socioId);
}