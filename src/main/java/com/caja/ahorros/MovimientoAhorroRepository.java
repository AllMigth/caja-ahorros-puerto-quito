package com.caja.ahorros;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoAhorroRepository extends JpaRepository<MovimientoAhorro, Long> {
    List<MovimientoAhorro> findBySocioIdOrderByFechaDesc(Long socioId);


    @Query("SELECT COALESCE(SUM(m.monto), 0) FROM MovimientoAhorro m " +
       "WHERE m.tipo = 'DEPOSITO' AND m.fecha <= :fecha")
    BigDecimal sumDepositosHastaFecha(@Param("fecha") LocalDateTime fecha);

    @Query("SELECT COALESCE(SUM(m.monto), 0) FROM MovimientoAhorro m " +
        "WHERE m.tipo = 'DEPOSITO' AND YEAR(m.fecha) = :anio AND MONTH(m.fecha) = :mes")
    BigDecimal sumDepositosDelMes(@Param("anio") int anio, @Param("mes") int mes);

    @Query("SELECT COALESCE(SUM(m.monto), 0) FROM MovimientoAhorro m " +
       "WHERE m.socio.id = :socioId AND m.tipo = 'DEPOSITO' AND m.fecha <= :fecha")
    BigDecimal sumDepositosPorSocioHastaFecha(@Param("socioId") Long socioId, @Param("fecha") LocalDateTime fecha);

    @Query("SELECT COALESCE(SUM(m.monto), 0) FROM MovimientoAhorro m " +
        "WHERE m.socio.id = :socioId AND m.tipo = 'DEPOSITO' " +
        "AND YEAR(m.fecha) = :anio AND MONTH(m.fecha) = :mes")
    BigDecimal sumDepositosPorSocioDelMes(@Param("socioId") Long socioId, 
                                        @Param("anio") int anio, 
                                        @Param("mes") int mes);

    @Query("SELECT s.saldoAhorros FROM Socio s WHERE s.id = :socioId")
    BigDecimal getSaldoActualPorSocio(@Param("socioId") Long socioId);
}