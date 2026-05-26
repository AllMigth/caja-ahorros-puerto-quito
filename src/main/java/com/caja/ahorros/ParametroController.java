package com.caja.ahorros;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/admin/parametros")
public class ParametroController {
    
    @Autowired
    private ParametroRepository parametroRepository;
    
    @GetMapping
    public List<ParametroSistema> listar() {
        return parametroRepository.findByActivoTrue();
    }
    
    @PutMapping("/{clave}")
    public ParametroSistema actualizar(@PathVariable String clave, @RequestBody String nuevoValor) {
        ParametroSistema param = parametroRepository.findById(clave)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        param.setValor(nuevoValor);
        return parametroRepository.save(param);
    }
}