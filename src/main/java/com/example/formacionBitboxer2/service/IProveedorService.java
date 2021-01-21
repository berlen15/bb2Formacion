package com.example.formacionBitboxer2.service;

import com.example.formacionBitboxer2.dto.ArticuloDTO;
import com.example.formacionBitboxer2.dto.ProveedorDTO;

import java.util.List;

public interface IProveedorService {
    public Iterable<ProveedorDTO> obtenerTodos();
    public ProveedorDTO obtenerPorNombre(String nombre);
    List<ArticuloDTO> articulosPorProveedor(String nombre);
    boolean guardarProveedor(ProveedorDTO proveedor);
}
