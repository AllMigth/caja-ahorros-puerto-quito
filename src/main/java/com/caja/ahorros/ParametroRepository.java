package com.caja.ahorros;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ParametroRepository extends JpaRepository<ParametroSistema, String> {
    List<ParametroSistema> findByActivoTrue();
}