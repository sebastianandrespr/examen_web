package com.example.demo.controllers;

import com.example.demo.entities.Pokemon;
import com.example.demo.repositories.PokemonRepository;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pokemon")
@Tag(name = "Pokemon", description = "Endpoints relacionados con los Pokemones")
public class PokemonController {

    private final PokemonRepository pokemonRepository;

    @Autowired
    public PokemonController(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    @GetMapping("/{tipo}")
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
}
