package com.example.demo.controllers;

import com.example.demo.dtos.LoginRequest;
import com.example.demo.dtos.LoginResponse;
import com.example.demo.entities.Captura;
import com.example.demo.entities.Entrenador;
import com.example.demo.entities.Pokemon;
import com.example.demo.repositories.CapturaRepository;
import com.example.demo.repositories.EntrenadorRepository;
import com.example.demo.repositories.PokemonRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entrenador")
@Tag(name = "Entrenador", description = "Endpoints relacionados con los entrenadores")
public class EntrenadorController {

    private final EntrenadorRepository entrenadorRepository;
    private final CapturaRepository capturaRepository;
    private final PokemonRepository pokemonRepository;

    @Autowired
    public EntrenadorController(EntrenadorRepository entrenadorRepository, CapturaRepository capturaRepository, PokemonRepository pokemonRepository) {
        this.entrenadorRepository = entrenadorRepository;
        this.capturaRepository = capturaRepository;
        this.pokemonRepository = pokemonRepository;
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
                    array = @ArraySchema(schema = @Schema(implementation = Pokemon.class)))),
            @ApiResponse(responseCode = "404", description = "Entrenador no encontrado",
                    content = @Content)
    })
    public ResponseEntity<List<Pokemon>> getPokemonsByEntrenador(
            @Parameter(description = "UUID del Entrenador", example = "f3262.....")
            @PathVariable("uuid") String uuid) {
        
        if (entrenadorRepository.findByUuid(uuid).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Pokemon> pokemons = capturaRepository.findByEntrenadorUuid(uuid).stream()
                .map(Captura::getPokemon)
                .collect(Collectors.toList());

        return ResponseEntity.ok(pokemons);
    }

    @PostMapping("/{uuid}/pokemons/{pokemonUuid}")
    @Operation(summary = "Agregar un pokemon al entrenador", description = "Asocia un pokemon no registrado a un entrenador específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pokemon registrado correctamente y lista retornada",
                    content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Pokemon.class)))),
            @ApiResponse(responseCode = "400", description = "Pokemon ya está registrado al entrenador",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"error\":\"true\", \"message\":\"pokemon ya está registrado al entrenador\"}"))),
            @ApiResponse(responseCode = "404", description = "Entrenador o pokemon no encontrado",
                    content = @Content)
    })
    public ResponseEntity<?> addPokemonToEntrenador(
            @Parameter(description = "UUID del Entrenador", example = "f3262.....")
            @PathVariable("uuid") String uuid,
            @Parameter(description = "UUID del Pokemon", example = "xxxxxxx")
            @PathVariable("pokemonUuid") String pokemonUuid) {

        return entrenadorRepository.findByUuid(uuid)
                .map(entrenador -> {
                    return pokemonRepository.findByUuid(pokemonUuid)
                            .map(pokemon -> {
                                if (capturaRepository.findByPokemonUuid(pokemonUuid).isPresent()) {
                                    Map<String, String> error = new HashMap<>();
                                    error.put("error", "true");
                                    error.put("message", "pokemon ya está registrado al entrenador");
                                    return ResponseEntity.badRequest().body(error);
                                }

                                Captura captura = new Captura(pokemon, entrenador);
                                capturaRepository.save(captura);

                                List<Pokemon> pokemons = capturaRepository.findByEntrenadorUuid(uuid).stream()
                                        .map(Captura::getPokemon)
                                        .collect(Collectors.toList());

                                return ResponseEntity.ok(pokemons);
                            })
                            .orElseGet(() -> ResponseEntity.notFound().build());
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
