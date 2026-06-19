package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class PokemonCreateRequest {
    private String nombre;
    private String descripcion;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate fechaDescubrimiento;

    private Integer generacion;
    private String tipoUuid;

    public PokemonCreateRequest() {
    }

    public PokemonCreateRequest(String nombre, String descripcion, LocalDate fechaDescubrimiento, Integer generacion, String tipoUuid) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaDescubrimiento = fechaDescubrimiento;
        this.generacion = generacion;
        this.tipoUuid = tipoUuid;
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

    public Integer getGeneracion() {
        return generacion;
    }

    public void setGeneracion(Integer generacion) {
        this.generacion = generacion;
    }

    public String getTipoUuid() {
        return tipoUuid;
    }

    public void setTipoUuid(String tipoUuid) {
        this.tipoUuid = tipoUuid;
    }
}
