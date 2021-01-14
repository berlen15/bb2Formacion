package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="proveedor")
public class Proveedor implements Serializable {
    @Id
    @Column(name="idproveedor")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idproveedor;

    @Column(name="nombre", nullable = false)
    private String nombre;

    @Column(name="pais")
    private String pais;


    @ManyToMany(mappedBy = "proveedores")
    private List<Articulo> articulos;

    public int getIdproveedor() {
        return idproveedor;
    }

    public void setIdproveedor(int idproveedor) {
        this.idproveedor = idproveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public List<Articulo> getArticulos() {
        return articulos;
    }

    public void setArticulos(List<Articulo> articulos) {
        this.articulos = articulos;
    }
}
