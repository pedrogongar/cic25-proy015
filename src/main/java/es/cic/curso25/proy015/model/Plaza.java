package es.cic.curso25.proy015.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "plaza")
public class Plaza {

    @Id
    private Integer id;

    @Version
    private Long version;

    @Size(max = 255)
    @Column(name = "descripcion", length = 255 ,nullable = true)
    private String descripcion;

    @OneToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_ocupante_id", unique = true)
    @JsonIgnore
    private Vehiculo vehiculoOcupante;

    public Plaza() {
    }

    public Plaza(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Vehiculo getVehiculoOcupante() {
        return vehiculoOcupante;
    }

    public void setVehiculoOcupante(Vehiculo vehiculoOcupante) {
        this.vehiculoOcupante = vehiculoOcupante;
    }

    public boolean isLibre() {
        return vehiculoOcupante == null;
    }

    public boolean isOcupada() {
        return vehiculoOcupante != null;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Plaza other = (Plaza) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Plaza{");
        sb.append("id=").append(id);
        sb.append(", descripcion=").append(descripcion);
        sb.append('}');
        return sb.toString();
    }

}
