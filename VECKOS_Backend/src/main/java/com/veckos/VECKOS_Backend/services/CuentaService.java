package com.veckos.VECKOS_Backend.services;

import com.veckos.VECKOS_Backend.dtos.cuenta.CuentaDto;
import com.veckos.VECKOS_Backend.dtos.cuenta.CuentaRequestDto;
import com.veckos.VECKOS_Backend.entities.Cuenta;
import com.veckos.VECKOS_Backend.repositories.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    public List<CuentaDto> obtenerTodasLasCuentas(){
        return this.cuentaRepository.findAll().
                stream().map(CuentaDto::new).toList();
    }

    public CuentaDto guardarCuenta(CuentaRequestDto cuentaRequestDto){
        Cuenta cuenta = new Cuenta();
        cuenta.setCbu(cuentaRequestDto.getCbu());
        cuenta.setDescripcion(cuentaRequestDto.getDescripcion());
        Cuenta guardada = this.cuentaRepository.save(cuenta);
        return new CuentaDto(guardada);
    }

    public CuentaDto editarCuenta(Long id, CuentaRequestDto cuentaRequestDto) {
        if(cuentaRepository.existsById(id)){
            Cuenta cuenta = cuentaRepository.findById(id).get();
            cuenta.setCbu(cuentaRequestDto.getCbu());
            cuenta.setDescripcion(cuentaRequestDto.getDescripcion());
            Cuenta actualizada = cuentaRepository.save(cuenta);
            return new CuentaDto(actualizada);
        }
        throw new RuntimeException("La cuenta con id : " + id + " no existe");
    }

    public Cuenta obtenerCuentaPorId(Long id){
        Optional<Cuenta> cuentaOptional = cuentaRepository.findById(id);
        if(cuentaOptional.isPresent()){
            return cuentaOptional.get();
        }else{
            throw new RuntimeException("La cuenta con id : " + id + " no existe");
        }
    }

    public CuentaDto obtenerCuentaDtoPorId(Long id) {
        Cuenta cuenta = obtenerCuentaPorId(id);
        return new CuentaDto(cuenta);
    }

    public void eliminarCuentaPorId(Long id) {
        if(cuentaRepository.existsById(id)){
            cuentaRepository.deleteById(id);
        }else{
            throw new RuntimeException("No existe la cuenta con ID: " + id);
        }
    }
}
