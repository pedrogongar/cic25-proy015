package es.cic.curso25.proy015.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.cic.curso25.proy015.model.Multa;
import es.cic.curso25.proy015.model.enums.EstadoMulta;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Long> {

    Optional<Multa> findByVehiculoSancionado(Long vehiculoId, EstadoMulta estado);
}
