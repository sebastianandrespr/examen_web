package com.example.demo.repositories;

import com.example.demo.entities.Captura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CapturaRepository extends JpaRepository<Captura, Long> {
    List<Captura> findByEntrenadorUuid(String uuid);
    Optional<Captura> findByPokemonUuid(String uuid);
}
