package es.cic.curso25.proy015.repository;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.cic.curso25.proy015.model.Multa;
import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.model.enums.EstadoMulta;
import es.cic.curso25.proy015.model.enums.MotivoMulta;

@SpringBootTest
@Transactional
class MultaRepositoryTest {

    @Autowired
    private MultaRepository multaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private PlazaRepository plazaRepository;

    @Test
    void buscarMultaAbiertaPorVehiculo_devuelveUna_siExiste() {

        Vehiculo coche = new Vehiculo();
        coche.setMatricula("ABC-444");
        vehiculoRepository.save(coche);

        Plaza plaza = new Plaza();
        plaza.setId(30);
        plaza.setDescripcion("PLAZA");
        plazaRepository.save(plaza);

        Multa multa = new Multa();
        multa.setVehiculoSancionado(coche);
        multa.setPlazaOcupada(plaza);
        multa.setFechaEntrada(OffsetDateTime.now());
        multa.setMotivo(MotivoMulta.PLAZA_NO_ASIGNADA);
        multa.setEstado(EstadoMulta.ABIERTA);
        multaRepository.save(multa);

        Optional<Multa> encontrada = multaRepository.findByVehiculoSancionadoIdYEstado(coche.getId(),
                EstadoMulta.ABIERTA);

        assertTrue(encontrada.isPresent());
        assertEquals(EstadoMulta.ABIERTA, encontrada.get().getEstado());
        assertEquals(coche.getId(), encontrada.get().getVehiculoSancionado().getId());
    }
}
