package es.cic.curso25.proy015.repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.model.Vehiculo;

@SpringBootTest
@Transactional
class VehiculoRepositoryTest {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private PlazaRepository plazaRepository;

    @Test
    void buscarPorMatricula_devuelveVehiculo_siExiste() {

        Vehiculo v = new Vehiculo();
        v.setMatricula("1234-ABC");
        v.setDescripcion("Coche de pruebas");
        vehiculoRepository.save(v);

        Optional<Vehiculo> encontrado = vehiculoRepository.findByMatricula("1234-ABC");

        assertTrue(encontrado.isPresent());
        assertEquals("1234-ABC", encontrado.get().getMatricula());
    }

    @Test
    void buscarPorMatricula_noDevuelveVehiculo_siNoExiste() {

        Optional<Vehiculo> noExiste = vehiculoRepository.findByMatricula("NO-EXISTE");
        assertFalse(noExiste.isPresent());
    }

    @Test
    void plazaAsignada_enVehiculo_puedeSerNula() {

        Plaza plaza = new Plaza();
        plaza.setId(1); 
        plaza.setDescripcion("Plaza 1");
        plazaRepository.save(plaza);

        Vehiculo sinPlaza = new Vehiculo();
        sinPlaza.setMatricula("SIN-PLAZA");
        vehiculoRepository.save(sinPlaza);

        Vehiculo conPlaza = new Vehiculo();
        conPlaza.setMatricula("CON-PLAZA");
        conPlaza.setPlazaAsignada(plaza);
        vehiculoRepository.save(conPlaza);

        Optional<Vehiculo> v1 = vehiculoRepository.findByMatricula("SIN-PLAZA");
        Optional<Vehiculo> v2 = vehiculoRepository.findByMatricula("CON-PLAZA");

        assertTrue(v1.isPresent());
        assertNull(v1.get().getPlazaAsignada());

        assertTrue(v2.isPresent());
        assertNotNull(v2.get().getPlazaAsignada());
        assertEquals(plaza.getId(), v2.get().getPlazaAsignada().getId());
    }
}
