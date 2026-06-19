package com.example.demo.controllers;

import com.example.demo.dtos.PokemonCreateRequest;
import com.example.demo.dtos.PokemonResponse;
import com.example.demo.entities.Pokemon;
import com.example.demo.entities.TipoPokemon;
import com.example.demo.repositories.PokemonRepository;
import com.example.demo.repositories.TipoPokemonRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Pokemon", description = "Endpoints relacionados con los Pokemones")
public class PokemonController {

    private final PokemonRepository pokemonRepository;
    private final TipoPokemonRepository tipoPokemonRepository;

    @Autowired
    public PokemonController(PokemonRepository pokemonRepository, TipoPokemonRepository tipoPokemonRepository) {
        this.pokemonRepository = pokemonRepository;
        this.tipoPokemonRepository = tipoPokemonRepository;
    }

    @GetMapping("/pokemon/{tipo}")
    @Operation(summary = "Listar pokemones por tipo", description = "Permite obtener todos los pokemones asociados a un tipo de pokémon específico usando su UUID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Pokemon.class))),
            @ApiResponse(responseCode = "400", description = "Parámetro inválido",
                    content = @Content)
    })
    public ResponseEntity<List<Pokemon>> getPokemonsByTipo(
            @Parameter(description = "UUID del Tipo de Pokémon", example = "f3262c24-47......")
            @PathVariable("tipo") String tipoUuid) {
        List<Pokemon> pokemons = pokemonRepository.findByTipoUuid(tipoUuid);
        return ResponseEntity.ok(pokemons);
    }

    @PostMapping("/pokemons")
    @Operation(summary = "Registrar un pokemon en el sistema", description = "Permite registrar un nuevo pokémon con su nombre, descripción, fecha de descubrimiento y opcionalmente generación y tipo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pokémon registrado exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PokemonResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content)
    })
    public ResponseEntity<PokemonResponse> createPokemon(@RequestBody PokemonCreateRequest request) {
        Pokemon pokemon = new Pokemon();
        pokemon.setNombre(request.getNombre());
        pokemon.setDescripcion(request.getDescripcion());
        pokemon.setFechaDescubrimiento(request.getFechaDescubrimiento());
        
        // Asignar generación por defecto (1) si no viene especificada
        int gen = (request.getGeneracion() != null) ? request.getGeneracion() : 1;
        pokemon.setGeneracion(gen);
        
        // Autogenerar UUID
        pokemon.setUuid(UUID.randomUUID().toString());

        // Buscar y asignar TipoPokemon si se provee uuid
        if (request.getTipoUuid() != null && !request.getTipoUuid().isBlank()) {
            tipoPokemonRepository.findAll().stream()
                    .filter(t -> request.getTipoUuid().equals(t.getUuid()))
                    .findFirst()
                    .ifPresent(pokemon::setTipo);
        }

        Pokemon saved = pokemonRepository.save(pokemon);

        PokemonResponse response = new PokemonResponse(
                String.valueOf(saved.getId()),
                saved.getNombre(),
                saved.getDescripcion(),
                saved.getFechaDescubrimiento(),
                String.valueOf(saved.getGeneracion()),
                saved.getUuid()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
