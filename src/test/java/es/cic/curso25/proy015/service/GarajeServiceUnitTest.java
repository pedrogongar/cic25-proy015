package es.cic.curso25.proy015.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import es.cic.curso25.proy015.model.Multa;
import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.model.enums.MotivoMulta;
import es.cic.curso25.proy015.repository.MultaRepository;
import es.cic.curso25.proy015.repository.PlazaRepository;
import es.cic.curso25.proy015.repository.VehiculoRepository;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
public class GarajeServiceUnitTest {

    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private PlazaRepository plazaRepository;

    @Mock
    private MultaRepository multaRepository;

    private GarajeService garajeService;

    private static final BigDecimal TARIFA = new BigDecimal("15.00");

    @BeforeEach
    void setUp() {
        garajeService = new GarajeService(vehiculoRepository, plazaRepository, multaRepository, TARIFA);
    }

    private Vehiculo vehiculo(Long id, String matricula, Plaza asignada) {

        Vehiculo coche = new Vehiculo();
        coche.setId(id);
        coche.setMatricula(matricula);
        coche.setPlazaAsignada(asignada);

        return coche;
    }

    private Plaza plaza(Integer id) {

        Plaza plaza = new Plaza();
        plaza.setId(id);

        return plaza;
    }

    private Multa multaAbierta(Long id, Vehiculo vehiculo, OffsetDateTime entrada, MotivoMulta motivo) {

        Multa multa = new Multa();
        multa.setId(id);
        multa.setVehiculoSancionado(vehiculo);
        multa.setFechaEntrada(entrada);
        multa.setMotivo(motivo);

        return multa;
    }

    @Nested
    @DisplayName("ENTRADA EN PLAZA ASIGNADA")
    class EntradaAsignada {

        @Test
        @DisplayName("Aparcar en plaza asignada: OK cuando la plaza asignada está libre")
        void aparcarEnPlazaAsignada_ok() {

            Plaza asignada = plaza(10);

            Vehiculo v = vehiculo(1L, "1234-ABC", asignada);

            when(vehiculoRepository.findByMatricula("1234-ABC")).thenReturn(Optional.of(v));
            when(plazaRepository.findByVehiculoOcupanteId(1L)).thenReturn(Optional.empty());
            when(plazaRepository.findById(10)).thenReturn(Optional.of(asignada));

            garajeService.aparcarEnPlazaAsignada("1234-ABC");

            assertEquals("1234-ABC", asignada.getVehiculoOcupante().getMatricula(),
                    "La plaza asignada debe quedar ocupada por el vehículo");

            verify(vehiculoRepository).findByMatricula("1234-ABC");
            verify(plazaRepository).findByVehiculoOcupanteId(1L);
            verify(plazaRepository).findById(10);
        }

        @Test
        @DisplayName("Falla si el vehículo ya está aparcado en alguna plaza")
        void aparcarEnPlazaAsignada_vehiculoYaAparcado() {

        }

        @Test
        @DisplayName("Falla si la plaza asignada está ocupada")
        void aparcarEnPlazaAsignada_plazaAsignadaOcupada() {

        }

        @Test
        @DisplayName("Falla si el vehículo no existe")
        void aparcarEnPlazaAsignada_vehiculoNoExiste() {

        }
    }

    @Nested
    @DisplayName("ENTRADA FORZADA (MULTA)")
    class EntradaForzada {

        @Test
        @DisplayName("Entrada forzada: ocupa plaza distinta y abre multa")
        void entradaForzada_abreMulta() {

        }

        @Test
        @DisplayName("Falla si el vehículo ya está aparcado en alguna plaza")
        void entradaForzada_vehiculoYaAparcado() {

        }

        @Test
        @DisplayName("Falla si la plaza de destino está ocupada")
        void entradaForzada_plazaOcupada() {

        }

        @Test
        @DisplayName("Falla si la plaza de destino no existe")
        void entradaForzada_plazaNoExiste() {

        }
    }

    @Nested
    @DisplayName("SALIDA DEL GARAJE")
    class Salida {

        @Test
        @DisplayName("Salida: libera plaza y cierra multa abierta con importe")
        void salida_cierraMulta() {

        }

        @Test
        @DisplayName("Salida: si el vehiculo no está aparcado, lanza excepción")
        void salida_vehiculoNoAparcado() {

        }

        @Test
        @DisplayName("Salida: si el vehículo no existe, lanza NoEncontradoException")
        void salida_vehiculoNoExiste() {

        }

        @Test
        @DisplayName("Salida: si no hay multa abierta, solo libera la plaza")
        void salida_sinMultaAbierta() {

        }
    }
}
