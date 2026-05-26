package com.caja.ahorros;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ParametroService {
    
    @Autowired
    private ParametroRepository parametroRepository;
    
    public BigDecimal getDecimal(String clave, BigDecimal defaultValue) {
        return parametroRepository.findById(clave)
                .map(ParametroSistema::getValorDecimal)
                .orElse(defaultValue);
    }
    
    public Integer getEntero(String clave, Integer defaultValue) {
        return parametroRepository.findById(clave)
                .map(ParametroSistema::getValorEntero)
                .orElse(defaultValue);
    }
}