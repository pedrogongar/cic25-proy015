package es.cic.curso25.proy015.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.cic.curso25.proy015.model.enums.EstadoMulta;
import es.cic.curso25.proy015.model.enums.MotivoMulta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "multa")
public class Multa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Version
    private Long version;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehiculo_sancionado_id", nullable = false)
    @JsonIgnore
    private Vehiculo vehiculoSancionado;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plaza_ocupada_id", nullable = false)
    @JsonIgnore
    private Plaza plazaOcupada;

    @NotNull
    @PastOrPresent
    @Column(name = "fecha_entrada", nullable = false)
    private OffsetDateTime fechaEntrada;

    @PastOrPresent
    @Column(name = "fecha_salida", nullable = true)
    private OffsetDateTime fechaSalida;

    @Positive
    @Column(name = "dias_aparcado", nullable = true)
    private Integer diasAparcado;

    @DecimalMin("0.00")
    @Column(name = "importe", nullable = true)
    private BigDecimal importe;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "motivo", nullable = false, length = 50)
    private MotivoMulta motivo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoMulta estado;

    public Multa() {
    }

    public Multa(Vehiculo vehiculoSancionado, Plaza plazaOcupada, OffsetDateTime fechaEntrada, OffsetDateTime fechaSalida, MotivoMulta motivo, EstadoMulta estado) {
        this.vehiculoSancionado = vehiculoSancionado;
        this.plazaOcupada = plazaOcupada;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.motivo = motivo;
        this.estado = estado;
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

    public Vehiculo getVehiculoSancionado() {
        return vehiculoSancionado;
    }

    public void setVehiculoSancionado(Vehiculo vehiculoSancionado) {
        this.vehiculoSancionado = vehiculoSancionado;
    }

    public Plaza getPlazaOcupada() {
        return plazaOcupada;
    }

    public void setPlazaOcupada(Plaza plazaOcupada) {
        this.plazaOcupada = plazaOcupada;
    }

    public OffsetDateTime getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(OffsetDateTime fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public OffsetDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(OffsetDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Integer getDiasAparcado() {
        return diasAparcado;
    }

    public void setDiasAparcado(Integer diasAparcado) {
        this.diasAparcado = diasAparcado;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public MotivoMulta getMotivo() {
        return motivo;
    }

    public void setMotivo(MotivoMulta motivo) {
        this.motivo = motivo;
    }

    public EstadoMulta getEstado() {
        return estado;
    }

    public void setEstado(EstadoMulta estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final Multa other = (Multa) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Multa{");
        sb.append("id=").append(id);
        sb.append(", fechaEntrada=").append(fechaEntrada);
        sb.append(", fechaSalida=").append(fechaSalida);
        sb.append(", diasAparcado=").append(diasAparcado);
        sb.append(", importe=").append(importe);
        sb.append(", motivo=").append(motivo);
        sb.append(", estado=").append(estado);
        sb.append('}');
        return sb.toString();
    }

}
