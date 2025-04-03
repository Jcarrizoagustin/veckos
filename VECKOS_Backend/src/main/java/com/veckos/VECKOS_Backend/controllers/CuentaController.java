package com.veckos.VECKOS_Backend.controllers;

import com.veckos.VECKOS_Backend.dtos.cuenta.CuentaDto;
import com.veckos.VECKOS_Backend.dtos.cuenta.CuentaRequestDto;
import com.veckos.VECKOS_Backend.services.CuentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @GetMapping
    public ResponseEntity<List<CuentaDto>> obtenerTodasLasCuentas(){
        return ResponseEntity.ok(cuentaService.obtenerTodasLasCuentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaDto> getCuentaPorId(@PathVariable Long id){
        return ResponseEntity.ok(cuentaService.obtenerCuentaDtoPorId(id));
    }


    @PostMapping
    public ResponseEntity<CuentaDto> postNuevaCuenta(@Valid @RequestBody CuentaRequestDto cuentaRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.cuentaService.guardarCuenta(cuentaRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaDto> postNuevaCuenta(@Valid @RequestBody CuentaRequestDto cuentaRequestDto, @PathVariable Long id){
        return ResponseEntity.ok(cuentaService.editarCuenta(id,cuentaRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuentaPorId(@PathVariable Long id){
        cuentaService.eliminarCuentaPorId(id);
        return ResponseEntity.noContent().build();
    }
}
