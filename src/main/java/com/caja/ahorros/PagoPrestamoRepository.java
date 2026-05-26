package com.caja.ahorros;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PagoPrestamoRepository extends JpaRepository<PagoPrestamo, Long> {
    List<PagoPrestamo> findByPrestamoIdOrderByNumeroCuotaAsc(Long prestamoId);
    Integer countByPrestamoId(Long prestamoId);

    @Query("SELECT COALESCE(SUM(p.montoPagado), 0) FROM PagoPrestamo p " +
       "WHERE p.fechaPago <= :fecha")
    BigDecimal sumPagosHastaFecha(@Param("fecha") LocalDateTime fecha);

    @Query("SELECT COALESCE(SUM(p.montoPagado), 0) FROM PagoPrestamo p " +
        "WHERE YEAR(p.fechaPago) = :anio AND MONTH(p.fechaPago) = :mes")
    BigDecimal sumPagosDelMes(@Param("anio") int anio, @Param("mes") int mes);

    
    @Query("SELECT COALESCE(SUM(p.montoPagado), 0) FROM PagoPrestamo p " +
       "WHERE p.prestamo.socio.id = :socioId AND p.fechaPago <= :fecha")
    BigDecimal sumPagosPorSocioHastaFecha(@Param("socioId") Long socioId, @Param("fecha") LocalDateTime fecha);

    @Query("SELECT COALESCE(SUM(p.montoPagado), 0) FROM PagoPrestamo p " +
        "WHERE p.prestamo.socio.id = :socioId " +
        "AND YEAR(p.fechaPago) = :anio AND MONTH(p.fechaPago) = :mes")
    BigDecimal sumPagosPorSocioDelMes(@Param("socioId") Long socioId, 
                                  @Param("anio") int anio, 
                                  @Param("mes") int mes);
}