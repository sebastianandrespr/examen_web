package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class PokemonResponse {
    private String id;
    private String nombre;
    private String descripcion;

    @JsonFormat(pattern = "yyyy/MM/dd")
    @JsonProperty("fecha_descubrimiento")
    private LocalDate fechaDescubrimiento;

    private String generacion;
    private String uuid;

    public PokemonResponse() {
    }

    public PokemonResponse(String id, String nombre, String descripcion, LocalDate fechaDescubrimiento, String generacion, String uuid) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaDescubrimiento = fechaDescubrimiento;
        this.generacion = generacion;
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaDescubrimiento() {
        return fechaDescubrimiento;
    }

    public void setFechaDescubrimiento(LocalDate fechaDescubrimiento) {
        this.fechaDescubrimiento = fechaDescubrimiento;
    }

    public String getGeneracion() {
        return generacion;
    }

    public void setGeneracion(String generacion) {
        this.generacion = generacion;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
