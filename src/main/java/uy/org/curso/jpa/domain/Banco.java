package uy.org.curso.jpa.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Banco {
    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    private String nombre;

    private String codigoBCU;

    public Banco() {
    }

    public Banco(String nombre, String codigoBCU) {
        this.nombre = nombre;
        this.codigoBCU = codigoBCU;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoBCU() {
        return codigoBCU;
    }

    public void setCodigoBCU(String codigoBCU) {
        this.codigoBCU = codigoBCU;
    }
}
