package es.cic.curso25.proy015.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.cic.curso25.proy015.exception.GarajeException;
import es.cic.curso25.proy015.exception.NoEncontradoException;
import es.cic.curso25.proy015.model.Multa;
import es.cic.curso25.proy015.model.Plaza;
import es.cic.curso25.proy015.model.Vehiculo;
import es.cic.curso25.proy015.model.enums.EstadoMulta;
import es.cic.curso25.proy015.model.enums.MotivoMulta;
import es.cic.curso25.proy015.repository.MultaRepository;
import es.cic.curso25.proy015.repository.PlazaRepository;
import es.cic.curso25.proy015.repository.VehiculoRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class GarajeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GarajeService.class.getName());

    private final VehiculoRepository vehiculoRepository;
    private final PlazaRepository plazaRepository;
    private final MultaRepository multaRepository;

    private final BigDecimal tarifaDiaria;

    public GarajeService(VehiculoRepository vehiculoRepository, PlazaRepository plazaRepository,
            MultaRepository multaRepository, @Value("${tarifa.diaria:15.00}") BigDecimal tarifaDiaria) {

        this.vehiculoRepository = vehiculoRepository;
        this.plazaRepository = plazaRepository;
        this.multaRepository = multaRepository;
        this.tarifaDiaria = tarifaDiaria;
    }

    public void aparcarEnPlazaAsignada(String matricula) {

        LOGGER.info("Entrada normal solicitada para matrícula: " + matricula);

        Vehiculo vehiculo = vehiculoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new NoEncontradoException("Vehículo no encontrado: " + matricula));

        if (plazaRepository.findByVehiculoOcupanteId(vehiculo.getId()).isPresent()) {

            LOGGER.warn("El vehículo " + matricula + " ya está aparcado");
            throw new GarajeException("El vehículo ya está aparcado en una plaza");
        }

        Plaza asignada = vehiculo.getPlazaAsignada();

        if (asignada == null) {

            LOGGER.warn("El vehículo " + matricula + "no tiene plaza asignada");
            throw new GarajeException("El vehículo no tiene una plaza asignada");
        }

        Plaza plazaAsignada = plazaRepository.findById(asignada.getId())
                .orElseThrow(() -> new NoEncontradoException("Plaza asignada no existe: " + asignada.getId()));

        if (plazaAsignada.getVehiculoOcupante() != null) {

            LOGGER.warn("La plaza asignada " + plazaAsignada.getId() + "está ocupada");
            throw new GarajeException("La plaza asignada está ocupada");
        }

        plazaAsignada.setVehiculoOcupante(vehiculo);
        LOGGER.info("Vehículo " + matricula + "aparcado en su plaza asignada " + plazaAsignada.getId());
    }

    public void aparcarEnPlazaNoAsignada(String matricula, Integer plazaId) {

        LOGGER.info("Entrada forzada solicitada para matrícula: " + matricula + " en plaza " + plazaId);

        Vehiculo vehiculo = vehiculoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new NoEncontradoException("Vehículo no encontrado: " + matricula));

        if (plazaRepository.findByVehiculoOcupanteId(vehiculo.getId()).isPresent()) {

            LOGGER.warn("El vehículo " + matricula + " ya está aparcado");
            throw new GarajeException("El vehículo ya está aparcado en una plaza");
        }

        Plaza destino = plazaRepository.findById(plazaId)
                .orElseThrow(() -> new NoEncontradoException("Plaza de destino no existe: " + plazaId));

        if (destino.getVehiculoOcupante() != null) {

            LOGGER.warn("La plaza " + plazaId + " está ocupada");
            throw new GarajeException("La plaza de destino está ocupada");
        }

        boolean esPlazaAsignada = vehiculo.getPlazaAsignada() != null
                && vehiculo.getPlazaAsignada().getId().equals(destino.getId());

        destino.setVehiculoOcupante(vehiculo);
        LOGGER.info("Vehículo " + matricula + " aparcado en plaza " + plazaId);

        if (!esPlazaAsignada) {
            Multa multa = new Multa();
            multa.setVehiculoSancionado(vehiculo);
            multa.setPlazaOcupada(destino);
            multa.setFechaEntrada(OffsetDateTime.now());
            multa.setMotivo(MotivoMulta.PLAZA_NO_ASIGNADA);
            multa.setEstado(EstadoMulta.ABIERTA);
            multaRepository.save(multa);

            LOGGER.info("Multa abierta para vehículo " + matricula + " por aparcar en plaza no asignada");
        }
    }

    public void salidaPorMatricula(String matricula) {

        LOGGER.info("Salida solicitada para matrícula: " + matricula);

        Vehiculo vehiculo = vehiculoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new NoEncontradoException("Vehículo no encontrado: " + matricula));

        Plaza plazaActual = plazaRepository.findByVehiculoOcupanteId(vehiculo.getId())
                .orElseThrow(() -> new GarajeException("El vehículo no está aparcado"));

        plazaActual.setVehiculoOcupante(null);

        LOGGER.info("Plaza " + plazaActual.getId() + " liberada por vehículo " + matricula);

        Optional<Multa> multaAbierta = multaRepository.findByVehiculoSancionadoIdYEstado(vehiculo.getId(),
                EstadoMulta.ABIERTA);

        if (multaAbierta.isPresent()) {

            Multa multa = multaAbierta.get();
            OffsetDateTime hoy = OffsetDateTime.now();
            multa.setFechaSalida(hoy);

            int dias = calcularDiasNaturales(multa.getFechaEntrada(), hoy);
            multa.setDiasAparcado(dias);

            BigDecimal importe = tarifaDiaria.multiply(BigDecimal.valueOf(dias));
            multa.setImporte(importe);

            multa.setEstado(EstadoMulta.CERRADA);

            LOGGER.info("Multa cerrada para vehículo " + matricula + " con importe: " + importe);
        } else {
            LOGGER.info("No había multa abierta para vehículo " + matricula);
        }
    }

    private int calcularDiasNaturales(OffsetDateTime inicio, OffsetDateTime fin) {

        LocalDate fechaInicio = inicio.toLocalDate();
        LocalDate fechaFinal = fin.toLocalDate();

        long diferencia = ChronoUnit.DAYS.between(fechaInicio, fechaFinal);

        if (diferencia < 1) {
            return 1;
        } else {
            return (int) diferencia;
        }
    }
}
