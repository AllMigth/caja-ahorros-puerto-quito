package com.caja.ahorros;

import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/calculadora")
public class AmortizacionController {

    @PostMapping("/amortizacion")
    public List<CuotaDTO> calcularAmortizacion(@RequestBody SimulacionDTO dto) {
        if ("ALEMAN".equalsIgnoreCase(dto.getSistema())) {
            return calcularAleman(dto.getMonto(), dto.getTasaMensual(), dto.getPlazoMeses());
        }
        return calcularFrances(dto.getMonto(), dto.getTasaMensual(), dto.getPlazoMeses());
    }
    
    // Sistema Alemán: Amortización fija, cuota decreciente
    private List<CuotaDTO> calcularAleman(BigDecimal monto, BigDecimal tasa, int plazo) {
        List<CuotaDTO> cuotas = new ArrayList<>();
        BigDecimal amortizacionFija = monto.divide(BigDecimal.valueOf(plazo), 2, RoundingMode.HALF_UP);
        BigDecimal saldo = monto;
        
        for (int i = 1; i <= plazo; i++) {
            BigDecimal interes = saldo.multiply(tasa)
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            BigDecimal cuota = amortizacionFija.add(interes);
            
            CuotaDTO c = new CuotaDTO(i, cuota, interes, amortizacionFija, saldo.subtract(amortizacionFija));
            cuotas.add(c);
            saldo = saldo.subtract(amortizacionFija);
        }
        return cuotas;
    }
    
    // Sistema Francés: Cuota fija, amortización creciente  
    private List<CuotaDTO> calcularFrances(BigDecimal monto, BigDecimal tasa, int plazo) {
        List<CuotaDTO> cuotas = new ArrayList<>();
        BigDecimal tasaDecimal = tasa.divide(new BigDecimal("100"), 8, RoundingMode.HALF_UP);
        
        // Fórmula: C = P * [i(1+i)^n] / [(1+i)^n - 1]
        BigDecimal unoMasI = BigDecimal.ONE.add(tasaDecimal);
        BigDecimal unoMasIPowN = unoMasI.pow(plazo);
        BigDecimal cuotaFija = monto.multiply(tasaDecimal.multiply(unoMasIPowN))
                .divide(unoMasIPowN.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
        
        BigDecimal saldo = monto;
        
        for (int i = 1; i <= plazo; i++) {
            BigDecimal interes = saldo.multiply(tasaDecimal).setScale(2, RoundingMode.HALF_UP);
            BigDecimal amortizacion = cuotaFija.subtract(interes);
            saldo = saldo.subtract(amortizacion);
            
            if (i == plazo) saldo = BigDecimal.ZERO; // Ajuste final por redondeo
            
            CuotaDTO c = new CuotaDTO(i, cuotaFija, interes, amortizacion, saldo);
            cuotas.add(c);
        }
        return cuotas;
    }
}