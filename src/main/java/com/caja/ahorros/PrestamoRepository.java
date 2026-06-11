package com.caja.ahorros;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    
    // NUEVO: Busca préstamo activo del socio
    Optional<Prestamo> findBySocioAndEstado(Socio socio, Prestamo.EstadoPrestamo estado);
    
    // NUEVO: Lista préstamos por estado
    List<Prestamo> findByEstado(Prestamo.EstadoPrestamo estado);
    
    // NUEVO: Cuenta préstamos activos de un socio
    @Query("SELECT COUNT(p) FROM Prestamo p WHERE p.socio.id = :socioId AND p.estado = 'APROBADO'")
    Long countPrestamosActivosPorSocio(@Param("socioId") Long socioId);
    
    // NUEVO: Suma saldo pendiente de préstamos activos del socio
    @Query("SELECT COALESCE(SUM(p.saldoPendiente), 0) FROM Prestamo p WHERE p.socio.id = :socioId AND p.estado = 'APROBADO'")
    BigDecimal sumSaldoPendientePorSocio(@Param("socioId") Long socioId);
    
    // NUEVO: Busca préstamos por socio ordenados
    List<Prestamo> findBySocioIdOrderByFechaSolicitudDesc(Long socioId);
}