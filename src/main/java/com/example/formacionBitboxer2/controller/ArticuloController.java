package com.example.formacionBitboxer2.controller;

import com.example.formacionBitboxer2.converter.ArticuloConverter;
import com.example.formacionBitboxer2.dto.ArticuloDTO;
import com.example.formacionBitboxer2.dto.ProveedorDTO;
import com.example.formacionBitboxer2.dto.ReduccionDTO;
import com.example.formacionBitboxer2.entities.Articulo;
import com.example.formacionBitboxer2.service.ArticuloService;
import com.example.formacionBitboxer2.service.ReduccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
public class ArticuloController implements ErrorController {

    @Autowired
    private ArticuloService articuloService;

    @Autowired
    private ReduccionService reduccionService;

    private ArticuloConverter articuloConverter = new ArticuloConverter();

    @GetMapping("/articulos")
    public List<ArticuloDTO> obtenerTodos(){
        return articuloService.obtenerTodos();
    }

    @GetMapping("/articulos/{codigo}")
    public ArticuloDTO obtenerPorCodigo(@PathVariable(name="codigo") Integer codigo){
        return articuloService.obtenerPorCodigoarticulo(codigo);
    }

    @GetMapping("/articulos/filter")
    public @ResponseBody List<ArticuloDTO>  obtenerPorFiltro(@RequestParam(name="estado") String estado) {
        List<ArticuloDTO> resultados = new ArrayList<>();
        if(estado.equals("Venta")){
            for(ArticuloDTO a : articuloService.obtenerTodos()){
                if(a.getEstado()==1){
                    resultados.add(a);
                }else{
                    continue;
                }
            }
        }else{
            for(ArticuloDTO a : articuloService.obtenerTodos()){
                if(a.getEstado()==2){
                    resultados.add(a);
                }else{
                    continue;
                }
            }
        }
        return resultados;
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/articulos/usuarios/{nombreusuario}")
    public List<ArticuloDTO> obtenerArticulosPorUsuario(@PathVariable(name="nombreusuario") String nombreusuario){
        return articuloService.obtenerTodosPorUsuario(nombreusuario);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/articulos")
    public ResponseEntity guardar(@RequestBody ArticuloDTO articuloDTO){
        if(articuloDTO!=null){
            articuloService.guardarArticulo(articuloDTO);
            return new ResponseEntity("Artículo creado con éxito",HttpStatus.CREATED);
        }else{
            return new ResponseEntity("El articulo no se ha creado correctamente. Supervise sus valores",HttpStatus.BAD_REQUEST);
        }

    }
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{nombre}/articulos/{codigo}")
    public ResponseEntity actualizar(@PathVariable("codigo") int codigo, @PathVariable("nombre") String nombreusuario, @RequestBody ArticuloDTO articuloDTO){
        System.out.println("Se ha entrado al actualizar");
        if(articuloDTO.getDescripcion()==null && articuloDTO.getEstado()==null && articuloDTO.getPrecio()==null){
            return new ResponseEntity("El artículo está vacío",HttpStatus.BAD_REQUEST);
        }
        Articulo articuloEditar = articuloConverter.dto2pojo(articuloService.obtenerPorCodigoarticulo(codigo));
        if(articuloEditar.getEstado()==2){
            return new ResponseEntity("No se pueden editar los artículos descatalogados", HttpStatus.BAD_REQUEST);
        }
        if(articuloEditar.getCreador().getNombreusuario().equals(nombreusuario)){
            if(articuloDTO.getPrecio()!=null){
                articuloEditar.setPrecio(articuloDTO.getPrecio());
            }
            if(articuloDTO.getDescripcion()!=null){
                articuloEditar.setDescripcion(articuloDTO.getDescripcion());
            }
            if(articuloDTO.getEstado()!=null){
                articuloEditar.setEstado(articuloDTO.getEstado());
            }
            articuloService.guardarArticulo(articuloConverter.pojo2dto(articuloEditar));
            return new ResponseEntity("Se ha editado el artículo",HttpStatus.ACCEPTED);
        }else{
            return new ResponseEntity("No tiene permiso para editar este artículo",HttpStatus.ACCEPTED);
        }

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/articulos/{codigo}/proveedores")
    public ResponseEntity añadirProveedor(@PathVariable("codigo") int codigo, @RequestBody ProveedorDTO proveedorDTO){
        System.out.println("Está entrando al añadir proveedor");
        if(proveedorDTO==null){
            return new ResponseEntity("El proveedor seleccionado no es válido", HttpStatus.BAD_REQUEST);
        }
        if(articuloService.addProveedor(codigo, proveedorDTO)){
            return new ResponseEntity("Se ha añadido el proveedor al artículo", HttpStatus.CREATED);
        }else{
            return new ResponseEntity("El proveedor ya está asociado al artículo", HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/{nombreusuario}/articulos/{codigo}/reducciones")
    public ResponseEntity añadirReduccion(@PathVariable("nombreusuario") String nombreusuario, @PathVariable("codigo") int codigo, @RequestBody ReduccionDTO reduccionDTO){
        if(reduccionDTO==null){
            return new ResponseEntity("La fecha de fin, ni creador ni la cantidad pueden estar vacíos", HttpStatus.BAD_REQUEST);
        }
        if(articuloService.addReduccion(codigo, nombreusuario, reduccionDTO)){
            return new ResponseEntity("Se ha añadido la reducción de precio al artículo", HttpStatus.CREATED);
        }
        return new ResponseEntity("No eres el creador del artículo", HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/articulos/{codigo}")
    public ResponseEntity eliminarArticulo( @PathVariable("codigo") int codigo){
        System.out.println("Entra al eliminar y elimina el "+codigo);
        if(articuloService.eliminarArticulo(codigo)){
           return new ResponseEntity("El articulo ha sido eliminado con éxito", HttpStatus.ACCEPTED);
       }else{
           return new ResponseEntity("No se ha eliminado correctamente el artículo", HttpStatus.BAD_REQUEST);
       }

    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
