package com.example.demo.controllers;

import com.example.demo.dtos.LoginRequest;
import com.example.demo.dtos.LoginResponse;
import com.example.demo.entities.Captura;
import com.example.demo.entities.Entrenador;
import com.example.demo.entities.Pokemon;
import com.example.demo.repositories.CapturaRepository;
import com.example.demo.repositories.EntrenadorRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entrenador")
@Tag(name = "Entrenador", description = "Endpoints relacionados con los entrenadores")
public class EntrenadorController {

    private final EntrenadorRepository entrenadorRepository;
    private final CapturaRepository capturaRepository;

    @Autowired
    public EntrenadorController(EntrenadorRepository entrenadorRepository, CapturaRepository capturaRepository) {
        this.entrenadorRepository = entrenadorRepository;
        this.capturaRepository = capturaRepository;
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

    @GetMapping("/{uuid}/pokemons")
    @Operation(summary = "Listar los pokemones de un entrenador", description = "Permite obtener todos los pokemones capturados por un entrenador específico a partir de su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pokemones obtenida con éxito",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Pokemon.class))),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado",
                    content = @Content)
    })
    public ResponseEntity<List<Pokemon>> getPokemonsByEntrenador(
            @Parameter(description = "UUID del Entrenador", example = "f3262.....")
            @PathVariable("uuid") String uuid) {
        
        // Primero verificamos si el entrenador existe por su UUID
        boolean entrenadorExists = entrenadorRepository.findAll().stream()
                .anyMatch(e -> uuid.equals(e.getUuid()));
        
        if (!entrenadorExists) {
            return ResponseEntity.notFound().build();
        }

        List<Pokemon> pokemons = capturaRepository.findByEntrenadorUuid(uuid).stream()
                .map(Captura::getPokemon)
                .collect(Collectors.toList());

        return ResponseEntity.ok(pokemons);
    }
}
