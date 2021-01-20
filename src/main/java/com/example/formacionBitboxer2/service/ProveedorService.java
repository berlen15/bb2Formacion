package com.example.formacionBitboxer2.service;

import com.example.formacionBitboxer2.converter.ArticuloConverter;
import com.example.formacionBitboxer2.converter.ProveedorConverter;
import com.example.formacionBitboxer2.dto.ArticuloDTO;
import com.example.formacionBitboxer2.dto.ProveedorDTO;
import com.example.formacionBitboxer2.entities.Articulo;
import com.example.formacionBitboxer2.entities.Proveedor;
import com.example.formacionBitboxer2.repository.IProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProveedorService implements IProveedorService{
    @Autowired
    private IProveedorRepository proveedorRepository;

    private ProveedorConverter proveedorConverter = new ProveedorConverter();
    private ArticuloConverter articuloConverter = new ArticuloConverter();
    @Override
    public List<ProveedorDTO> obtenerTodos() {
        List<ProveedorDTO> proveedores = new ArrayList<>();
        for(Proveedor p : proveedorRepository.findAll()){
            List<ArticuloDTO> articulos = articuloConverter.convertAllToDTO(p.getArticulos());
            ProveedorDTO proveedorDTO = proveedorConverter.pojo2dto(p);
            proveedorDTO.setArticulos(articulos);
            proveedores.add(proveedorDTO);
        }
        return proveedores;
    }

    @Override
    public ProveedorDTO obtenerPorId(Integer idproveedor) {
        return proveedorConverter.pojo2dto(proveedorRepository.findByIdproveedor(idproveedor));
    }

    @Override
    public List<ArticuloDTO> articulosPorProveedor(int idproveedor) {
        Proveedor proveedor = proveedorRepository.findByIdproveedor(idproveedor);
        return articuloConverter.convertAllToDTO(proveedor.getArticulos());
    }


}