package es.cic.curso25.proy015.repository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.model.Vehiculo;

@SpringBootTest
@Transactional
class PlazaRepositoryTest {

    @Autowired
    private PlazaRepository plazaRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Test
    void listarPlazasLibres_devuelveSoloLasQueNoTienenOcupante() {

        Plaza p1 = new Plaza();
        p1.setId(1);
        p1.setDescripcion("P1");

        Plaza p2 = new Plaza();
        p2.setId(2);
        p2.setDescripcion("P2");

        Plaza p3 = new Plaza();
        p3.setId(3);
        p3.setDescripcion("P3");

        plazaRepository.saveAll(List.of(p1, p2, p3));

        Vehiculo coche = new Vehiculo();
        coche.setMatricula("ABC-111");
        vehiculoRepository.save(coche);

        p2.setVehiculoOcupante(coche);
        plazaRepository.save(p2);

        List<Plaza> libres = plazaRepository.findByVehiculoOcupanteIsNull();

        assertEquals(2, libres.size());

        boolean contieneP1 = false;
        boolean contieneP3 = false;

        for (Plaza plaza : libres) {
            if ("P1".equals(plaza.getDescripcion())) {
                contieneP1 = true;
            }
            if ("P3".equals(plaza.getDescripcion())) {
                contieneP3 = true;
            }
        }

        assertTrue(contieneP1);
        assertTrue(contieneP3);

    }

    @Test
    void listarPlazasOcupadas_devuelveSoloLasQueTienenOcupante() {

        Plaza libre = new Plaza();
        libre.setId(10);
        libre.setDescripcion("LIBRE");

        Plaza ocupada = new Plaza();
        ocupada.setId(11);
        ocupada.setDescripcion("OCUPADA");

        plazaRepository.saveAll(List.of(libre, ocupada));

        Vehiculo coche = new Vehiculo();
        coche.setMatricula("ABC-222");
        vehiculoRepository.save(coche);

        ocupada.setVehiculoOcupante(coche);
        plazaRepository.save(ocupada);

        List<Plaza> ocupadas = plazaRepository.findByVehiculoOcupanteIsNotNull();

        assertEquals(1, ocupadas.size());
        assertEquals("OCUPADA", ocupadas.get(0).getDescripcion());
    }

    @Test
    void encontrarPlazaPorVehiculoOcupante_devuelveLaPlazaCorrecta() {

        Plaza plaza = new Plaza();
        plaza.setId(20);
        plaza.setDescripcion("PLAZA");
        plazaRepository.save(plaza);

        Vehiculo coche = new Vehiculo();
        coche.setMatricula("ABC-333");
        vehiculoRepository.save(coche);

        plaza.setVehiculoOcupante(coche);
        plazaRepository.save(plaza);

        Optional<Plaza> encontrada = plazaRepository.findByVehiculoOcupanteId(coche.getId());

        assertTrue(encontrada.isPresent());
        assertEquals("PLAZA", encontrada.get().getDescripcion());
    }
}
