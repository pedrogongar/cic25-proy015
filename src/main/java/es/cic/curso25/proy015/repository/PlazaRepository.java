package es.cic.curso25.proy015.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.cic.curso25.proy015.model.Plaza;

@Repository
public interface PlazaRepository extends JpaRepository<Plaza, Integer> {

    List<Plaza> findByVehiculoOcupanteIsNull();

    List<Plaza> findByVehiculoOcupanteIsNotNull();

    Optional<Plaza> findByVehiculoOcupanteId(Long vehiculoId);
}
