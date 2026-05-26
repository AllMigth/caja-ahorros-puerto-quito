package com.caja.ahorros;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CuotaPrestamoRepository extends JpaRepository<CuotaPrestamo, Long> {
    List<CuotaPrestamo> findByPrestamoIdOrderByNumeroCuotaAsc(Long prestamoId);
    
    void deleteByPrestamoId(Long prestamoId);
    
    @Query("SELECT c FROM CuotaPrestamo c WHERE c.prestamo.id = :prestamoId AND c.estado = 'PENDIENTE' ORDER BY c.numeroCuota ASC")
    List<CuotaPrestamo> findCuotasPendientesPorPrestamo(@Param("prestamoId") Long prestamoId);
}