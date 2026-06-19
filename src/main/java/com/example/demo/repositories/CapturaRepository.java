package com.example.demo.repositories;

import com.example.demo.entities.Captura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapturaRepository extends JpaRepository<Captura, Long> {
    List<Captura> findByEntrenadorUuid(String uuid);
}
