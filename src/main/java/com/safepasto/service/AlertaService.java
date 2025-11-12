package com.safepasto.service;

import com.safepasto.model.User;
import com.safepasto.model.dto.AlertaRequest;
import com.safepasto.model.dto.AlertaResponse;
import java.util.List;

public interface AlertaService {
    AlertaResponse crearAlerta(AlertaRequest request, User usuario);
    List<AlertaResponse> obtenerAlertasActivas();
    List<AlertaResponse> obtenerAlertasPorTipo(String tipo);
    List<AlertaResponse> obtenerAlertasPorUsuario(Long usuarioId);
    AlertaResponse obtenerAlertaPorId(Long id);
    void marcarComoResuelta(Long id);
    void verificarAlerta(Long id);
    List<AlertaResponse> obtenerAlertasRecientes();
    List<AlertaResponse> obtenerAlertasCercanas(Double latitud, Double longitud, Double radioKm);
}