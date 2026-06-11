package com.caja.ahorros;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    
    List<Prestamo> findBySocioIdOrderByFechaSolicitudDesc(Long socioId);
    
    List<Prestamo> findByEstado(Prestamo.EstadoPrestamo estado);

    @Query("SELECT COALESCE(SUM(p.saldoPendiente), 0) FROM Prestamo p " +
       "WHERE p.socio.id = :socioId AND p.estado = 'APROBADO'")
    BigDecimal sumSaldoPendientePorSocio(@Param("socioId") Long socioId);

    @Query("SELECT COUNT(p) FROM Prestamo p " +
        "WHERE p.socio.id = :socioId AND p.estado = 'APROBADO'")
    Integer countPrestamosActivosPorSocio(@Param("socioId") Long socioId);
    
    Optional<Prestamo> findBySocioAndEstado(Socio socio, Prestamo.EstadoPrestamo estado);

}