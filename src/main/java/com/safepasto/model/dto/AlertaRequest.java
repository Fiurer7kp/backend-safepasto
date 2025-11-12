package com.safepasto.model.dto;

import java.time.LocalDateTime;

public class AlertaRequest {
    private String titulo;
    private String descripcion;
    private String tipo;
    private String severidad;
    private Long usuarioId;

    // Constructores
    public AlertaRequest() {}

    public AlertaRequest(String titulo, String descripcion, String tipo, String severidad, Long usuarioId) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.severidad = severidad;
        this.usuarioId = usuarioId;
    }

    // Getters y Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getSeveridad() { return severidad; }
    public void setSeveridad(String severidad) { this.severidad = severidad; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}