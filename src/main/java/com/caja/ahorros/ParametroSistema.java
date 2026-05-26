package com.caja.ahorros;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "parametro_sistema")
public class ParametroSistema {
    
    @Id
    private String clave; // "PRESTAMO_MAX_MULTIPLO", "PRESTAMO_TASA_MAX", etc
    
    @Column(nullable = false)
    private String valor; // "5", "5.0", "50"
    
    @Column(nullable = false)
    private String tipo; // "INTEGER", "DECIMAL", "STRING"
    
    @Column(nullable = false)
    private String descripcion; // "Máximo múltiplo del saldo para prestar"
    
    private Boolean activo = true;

    // Getters y Setters
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    // Helpers para convertir
    public BigDecimal getValorDecimal() {
        return new BigDecimal(valor);
    }
    
    public Integer getValorEntero() {
        return Integer.parseInt(valor);
    }
}