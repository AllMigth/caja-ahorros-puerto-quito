package com.caja.ahorros;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "socio")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "Nombres entre 2 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    private String apellidos;

    @NotBlank(message = "La cédula es obligatoria")
    @Size(min = 10, max = 20)
    @Column(unique = true, nullable = false, length = 20)
    private String cedula;

    @Pattern(regexp = "^09[0-9]{8}$", message = "Teléfono debe ser celular: 09xxxxxxxx")
    @Column(length = 15)
    private String telefono;

    @Size(max = 200)
    @Column(length = 200)
    private String direccion;

    private LocalDate fechaIngreso;

    @DecimalMin(value = "0.0", message = "El saldo no puede ser negativo")
    @Column(precision = 10, scale = 2)
    private BigDecimal saldoAhorros = BigDecimal.ZERO;

    // Constructores, getters y setters igual que antes...
    
    public Socio() {}
    
    public Socio(String nombres, String apellidos, String cedula) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.fechaIngreso = LocalDate.now();
    }

    // Getters y Setters... igual que antes
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public BigDecimal getSaldoAhorros() { return saldoAhorros; }
    public void setSaldoAhorros(BigDecimal saldoAhorros) { this.saldoAhorros = saldoAhorros; }
}