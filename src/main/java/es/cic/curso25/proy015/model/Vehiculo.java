package es.cic.curso25.proy015.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "vehiculo")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Version
    private Long version;

    @NotBlank
    @Size(max = 16)
    @Column(name = "matricula", length = 16, nullable = false, unique = true)
    private String matricula;

    @Size(max = 255)
    @Column(name = "descripcion", length = 255, nullable = true)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY ,optional = true)
    @JoinColumn(name = "plaza_asignada_id")
    @JsonIgnore
    private Plaza plazaAsignada;

    public Vehiculo() {
    }

    public Vehiculo(String matricula, String descripcion, Plaza plazaAsignada) {
        this.matricula = matricula;
        this.descripcion = descripcion;
        this.plazaAsignada = plazaAsignada;
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

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Plaza getPlazaAsignada() {
        return plazaAsignada;
    }

    public void setPlazaAsignada(Plaza plazaAsignada) {
        this.plazaAsignada = plazaAsignada;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vehiculo other = (Vehiculo) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vehiculo{");
        sb.append("id=").append(id);
        sb.append(", matricula=").append(matricula);
        sb.append(", descripcion=").append(descripcion);
        sb.append('}');
        return sb.toString();
    }

}
