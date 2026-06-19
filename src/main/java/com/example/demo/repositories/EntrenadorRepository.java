package com.example.demo.repositories;

import com.example.demo.entities.Entrenador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntrenadorRepository extends JpaRepository<Entrenador, Integer> {
    Optional<Entrenador> findByEmail(String email);
    Optional<Entrenador> findByUuid(String uuid);
}
