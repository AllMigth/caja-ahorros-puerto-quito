package com.caja.ahorros;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Long> {
    
    Optional<Socio> findByCedula(String cedula);
    
    boolean existsByCedula(String cedula);
    
    // NUEVO: Busca por nombres o apellidos
    @Query("SELECT s FROM Socio s WHERE LOWER(CONCAT(s.nombres, ' ', s.apellidos)) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Socio> buscarPorNombre(String nombre);
}