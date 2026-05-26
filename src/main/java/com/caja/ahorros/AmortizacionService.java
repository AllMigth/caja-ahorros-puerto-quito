package com.caja.ahorros;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AmortizacionService {

    @Autowired
    private CuotaPrestamoRepository cuotaPrestamoRepository;

    @Transactional
    public List<CuotaPrestamo> generarTabla(Prestamo prestamo) {
        cuotaPrestamoRepository.deleteByPrestamoId(prestamo.getId());
        
        return switch (prestamo.getSistemaAmortizacion()) {
            case FRANCES -> generarTablaFrancesa(prestamo);
            case ALEMAN -> generarTablaAlemana(prestamo);
        };
    }

    private List<CuotaPrestamo> generarTablaFrancesa(Prestamo prestamo) {
        BigDecimal monto = prestamo.getMontoAprobado();
        BigDecimal tasaMensual = BigDecimal.valueOf(prestamo.getTasaInteresMensual())
                .divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP);
        int plazo = prestamo.getPlazoMeses();
        
        BigDecimal unoMasI = BigDecimal.ONE.add(tasaMensual);
        BigDecimal unoMasIPowN = unoMasI.pow(plazo);
        
        BigDecimal numerador = tasaMensual.multiply(unoMasIPowN);
        BigDecimal denominador = unoMasIPowN.subtract(BigDecimal.ONE);
        BigDecimal cuotaFija = monto.multiply(numerador)
                .divide(denominador, 2, RoundingMode.HALF_UP);
        
        List<CuotaPrestamo> cuotas = new ArrayList<>();
        BigDecimal saldo = monto;
        LocalDate fechaInicio = prestamo.getFechaAprobacion().toLocalDate();
        
        for (int i = 1; i <= plazo; i++) {
            BigDecimal interes = saldo.multiply(tasaMensual).setScale(2, RoundingMode.HALF_UP);
            BigDecimal capital = cuotaFija.subtract(interes);
            
            if (capital.compareTo(saldo) > 0 || i == plazo) {
                capital = saldo;
                cuotaFija = capital.add(interes).setScale(2, RoundingMode.HALF_UP);
            }
            
            saldo = saldo.subtract(capital);
            if (saldo.compareTo(BigDecimal.ZERO) < 0) saldo = BigDecimal.ZERO;
            
            CuotaPrestamo cuota = new CuotaPrestamo();
            cuota.setNumeroCuota(i);
            cuota.setFechaVencimiento(fechaInicio.plusMonths(i));
            cuota.setCapital(capital.setScale(2, RoundingMode.HALF_UP));
            cuota.setInteres(interes);
            cuota.setCuotaTotal(cuotaFija);
            cuota.setSaldoPendiente(saldo.setScale(2, RoundingMode.HALF_UP));
            cuota.setPrestamo(prestamo);
            cuota.setEstado(CuotaPrestamo.EstadoCuota.PENDIENTE);
            
            cuotas.add(cuota);
            
            if (saldo.compareTo(BigDecimal.ZERO) == 0) break;
        }
        
        return cuotaPrestamoRepository.saveAll(cuotas);
    }

    private List<CuotaPrestamo> generarTablaAlemana(Prestamo prestamo) {
        BigDecimal monto = prestamo.getMontoAprobado();
        BigDecimal tasaMensual = BigDecimal.valueOf(prestamo.getTasaInteresMensual())
                .divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP);
        int plazo = prestamo.getPlazoMeses();
        
        // Capital fijo = monto / plazo
        BigDecimal capitalFijo = monto.divide(BigDecimal.valueOf(plazo), 2, RoundingMode.HALF_UP);
        
        List<CuotaPrestamo> cuotas = new ArrayList<>();
        BigDecimal saldo = monto;
        LocalDate fechaInicio = prestamo.getFechaAprobacion().toLocalDate();
        
        for (int i = 1; i <= plazo; i++) {
            BigDecimal interes = saldo.multiply(tasaMensual).setScale(2, RoundingMode.HALF_UP);
            BigDecimal capital = capitalFijo;
            
            // Ajuste última cuota por redondeo
            if (i == plazo) {
                capital = saldo;
            }
            
            BigDecimal cuotaTotal = capital.add(interes).setScale(2, RoundingMode.HALF_UP);
            saldo = saldo.subtract(capital);
            if (saldo.compareTo(BigDecimal.ZERO) < 0) saldo = BigDecimal.ZERO;
            
            CuotaPrestamo cuota = new CuotaPrestamo();
            cuota.setNumeroCuota(i);
            cuota.setFechaVencimiento(fechaInicio.plusMonths(i));
            cuota.setCapital(capital);
            cuota.setInteres(interes);
            cuota.setCuotaTotal(cuotaTotal);
            cuota.setSaldoPendiente(saldo.setScale(2, RoundingMode.HALF_UP));
            cuota.setPrestamo(prestamo);
            cuota.setEstado(CuotaPrestamo.EstadoCuota.PENDIENTE);
            
            cuotas.add(cuota);
        }
        
        return cuotaPrestamoRepository.saveAll(cuotas);
    }
}