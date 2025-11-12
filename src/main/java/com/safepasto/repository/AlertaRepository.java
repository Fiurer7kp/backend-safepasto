package com.safepasto.repository;

import com.safepasto.model.Alerta;
import com.safepasto.model.TipoAlerta;
import com.safepasto.model.GravedadAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    
    List<Alerta> findByActivaTrueOrderByCreatedAtDesc();
    
    List<Alerta> findByTipoAlertaAndActivaTrue(TipoAlerta tipoAlerta);
    
    List<Alerta> findByGravedadAndActivaTrue(GravedadAlerta gravedad);
    
    List<Alerta> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);
    
    @Query("SELECT a FROM Alerta a WHERE a.activa = true AND a.createdAt >= :fecha")
    List<Alerta> findAlertasRecientes(@Param("fecha") LocalDateTime fecha);
    
    @Query("SELECT a FROM Alerta a WHERE a.activa = true AND " +
           "a.ubicacion.latitud BETWEEN :latMin AND :latMax AND " +
           "a.ubicacion.longitud BETWEEN :lonMin AND :lonMax")
    List<Alerta> findAlertasCercanas(@Param("latMin") Double latMin, 
                                    @Param("latMax") Double latMax,
                                    @Param("lonMin") Double lonMin, 
                                    @Param("lonMax") Double lonMax);
    
    long countByActivaTrue();
}