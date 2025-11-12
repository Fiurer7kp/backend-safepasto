package com.safepasto.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Ubicacion {
    private Double latitud;
    private Double longitud;
    private String direccion;
    private String barrio;
    private String ciudad = "Pasto";
    private String departamento = "Nariño";
    
    // Constructors
    public Ubicacion() {}
    
    public Ubicacion(Double latitud, Double longitud, String direccion, String barrio) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.direccion = direccion;
        this.barrio = barrio;
    }
    
    // Getters and Setters
    public Double getLatitud() { return latitud; }
    public void setLatitud(Double latitud) { this.latitud = latitud; }
    
    public Double getLongitud() { return longitud; }
    public void setLongitud(Double longitud) { this.longitud = longitud; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    
    public String getBarrio() { return barrio; }
    public void setBarrio(String barrio) { this.barrio = barrio; }
    
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    
    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
}