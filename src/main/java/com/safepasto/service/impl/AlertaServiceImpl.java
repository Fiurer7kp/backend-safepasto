package com.safepasto.service.impl;

import com.safepasto.model.Alerta;
import com.safepasto.model.GravedadAlerta;
import com.safepasto.model.TipoAlerta;
import com.safepasto.model.User;
import com.safepasto.model.dto.AlertaRequest;
import com.safepasto.model.dto.AlertaResponse;
import com.safepasto.repository.AlertaRepository;
import com.safepasto.service.AlertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertaServiceImpl implements AlertaService {

    @Autowired
    private AlertaRepository alertaRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public AlertaResponse crearAlerta(AlertaRequest request, User usuario) {
        Alerta alerta = new Alerta();
        alerta.setUsuario(usuario);
        alerta.setDescripcion(request.getDescripcion());
        try {
            alerta.setTipoAlerta(TipoAlerta.valueOf(request.getTipo()));
        } catch (Exception e) {
            alerta.setTipoAlerta(TipoAlerta.OTRO);
        }
        try {
            alerta.setGravedad(GravedadAlerta.valueOf(request.getSeveridad()));
        } catch (Exception e) {
            alerta.setGravedad(GravedadAlerta.MEDIA);
        }

        Alerta saved = alertaRepository.save(alerta);
        AlertaResponse response = toResponse(saved);
        notificationService.sendAlertNotification(response);
        return response;
    }

    @Override
    public List<AlertaResponse> obtenerAlertasActivas() {
        return alertaRepository.findByActivaTrueOrderByCreatedAtDesc()
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<AlertaResponse> obtenerAlertasPorTipo(String tipo) {
        TipoAlerta t;
        try { t = TipoAlerta.valueOf(tipo); } catch (Exception e) { t = TipoAlerta.OTRO; }
        return alertaRepository.findByTipoAlertaAndActivaTrue(t)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<AlertaResponse> obtenerAlertasPorUsuario(Long usuarioId) {
        return alertaRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public AlertaResponse obtenerAlertaPorId(Long id) {
        Alerta a = alertaRepository.findById(id).orElseThrow();
        return toResponse(a);
    }

    @Override
    public void marcarComoResuelta(Long id) {
        Alerta a = alertaRepository.findById(id).orElseThrow();
        a.setActiva(false);
        alertaRepository.save(a);
    }

    @Override
    public void verificarAlerta(Long id) {
        Alerta a = alertaRepository.findById(id).orElseThrow();
        a.setVerificada(true);
        alertaRepository.save(a);
    }

    @Override
    public List<AlertaResponse> obtenerAlertasRecientes() {
        LocalDateTime desde = LocalDateTime.now().minusHours(24);
        return alertaRepository.findAlertasRecientes(desde)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<AlertaResponse> obtenerAlertasCercanas(Double latitud, Double longitud, Double radioKm) {
        double latDelta = radioKm / 111.0; // approximation for degrees per km
        double lonDelta = radioKm / 111.0;
        return alertaRepository.findAlertasCercanas(latitud - latDelta, latitud + latDelta,
                longitud - lonDelta, longitud + lonDelta)
                .stream().map(this::toResponse).toList();
    }

    private AlertaResponse toResponse(Alerta a) {
        AlertaResponse r = new AlertaResponse();
        r.setId(a.getId());
        r.setTitulo(a.getTipoAlerta() != null ? a.getTipoAlerta().name() : "ALERTA");
        r.setDescripcion(a.getDescripcion());
        r.setTipo(a.getTipoAlerta() != null ? a.getTipoAlerta().name() : null);
        r.setSeveridad(a.getGravedad() != null ? a.getGravedad().name() : null);
        r.setFechaCreacion(a.getCreatedAt());
        r.setFechaActualizacion(a.getUpdatedAt());
        if (a.getUsuario() != null) {
            r.setUsuarioId(a.getUsuario().getId());
            r.setUsuarioNombre(a.getUsuario().getUsername());
        }
        return r;
    }
}