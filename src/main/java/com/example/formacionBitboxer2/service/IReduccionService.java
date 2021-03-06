package com.example.formacionBitboxer2.service;

import com.example.formacionBitboxer2.dto.ReduccionDTO;
import com.example.formacionBitboxer2.entities.Reduccion;

public interface IReduccionService {
    public void asociarArticulo(int codigo, ReduccionDTO reduccionDTO);
    public ReduccionDTO obtenerPorCodigoreduccion(Long codigo);
    public void guardarReduccion(Reduccion reduccion);

}
