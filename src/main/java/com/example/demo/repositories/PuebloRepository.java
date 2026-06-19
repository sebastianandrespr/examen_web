package com.example.demo.repositories;

import com.example.demo.entities.Pueblo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuebloRepository extends JpaRepository<Pueblo, Integer> {
}
