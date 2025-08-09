# proy015 — Garaje (Vehículo, Plaza, Multa)

**Autor:** Pedro González García

## Descripción

Sistema didáctico para gestionar un garaje con **150 plazas** precargadas y tres entidades de dominio:

- **Vehículo:** matrícula única y una **plaza asignada (preferente)** opcional.
- **Plaza:** estado **LIBRE/OCUPADA** derivado de si tiene **vehículo ocupante**.
- **Multa:** histórico de infracciones cuando un vehículo aparca en **plaza distinta** a su asignada (se abre al entrar forzado y se cierra al salir).

El foco del proyecto está en el **modelo de dominio**, **validaciones** y **consistencia** (sin complicar con históricos de ocupaciones fuera de Multa).

## Stack

- Java 17, Spring Boot 3.5.x
- Spring Data JPA (Hibernate 6)
- Validación: Jakarta Bean Validation (Hibernate Validator)
- BD: H2 (dev) / PostgreSQL (runtime)
- Testcontainers (tests de integración)

## Reglas de negocio clave

- Un vehículo ocupa como máximo **una** plaza a la vez.
- Una plaza tiene **0..1** ocupante; si no hay ocupante → **LIBRE**.
- **Entrada normal:** solo a su plaza asignada, y si está **libre**.
- **Entrada forzada:** a otra plaza **libre** → **abre Multa**.
- **Salida:** libera la plaza y **cierra** la Multa (cálculo de días × tarifa).
