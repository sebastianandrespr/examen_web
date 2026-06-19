package com.example.demo.controllers;

import com.example.demo.dtos.LoginRequest;
import com.example.demo.dtos.LoginResponse;
import com.example.demo.entities.Entrenador;
import com.example.demo.repositories.EntrenadorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entrenador")
@Tag(name = "Entrenador", description = "Endpoints relacionados con los entrenadores")
public class EntrenadorController {

    private final EntrenadorRepository entrenadorRepository;

    @Autowired
    public EntrenadorController(EntrenadorRepository entrenadorRepository) {
        this.entrenadorRepository = entrenadorRepository;
    }

    @PostMapping("/login")
    @Operation(summary = "Obtener el UUID de un entrenador registrado", description = "Permite obtener el uuid de un entrenador utilizando su correo electrónico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso, retorna el UUID",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado",
                    content = @Content)
    })
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return entrenadorRepository.findByEmail(loginRequest.getEmail())
                .map(entrenador -> ResponseEntity.ok(new LoginResponse(entrenador.getUuid())))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
