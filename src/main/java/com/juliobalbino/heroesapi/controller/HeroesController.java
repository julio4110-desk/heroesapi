package com.juliobalbino.heroesapi.controller;

import com.juliobalbino.heroesapi.document.Heroes;
import com.juliobalbino.heroesapi.repository.HeroesRepository;
import com.juliobalbino.heroesapi.service.HeroesService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.juliobalbino.heroesapi.constans.HeroesConstant.HEROES_ENDPOINT_LOCAL;


@RestController
@RequestMapping(value = HEROES_ENDPOINT_LOCAL)
public class HeroesController {

    HeroesService heroesService;
    HeroesRepository heroesRepository;

    private static final org.slf4j.Logger log=
            org.slf4j.LoggerFactory.getLogger(HeroesController.class);

    public HeroesController (HeroesService heroesService, HeroesRepository heroesRepository){
        this.heroesRepository = heroesRepository;
        this.heroesService = heroesService;
    }

    @GetMapping
    public Flux<Heroes> getAllItems(){
        log.info("Requesting the list of all heroes");
        return heroesService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<Heroes>> findByIdHero(@PathVariable String id){
        log.info("requesting the hero with id {}", id);
        return heroesService.findByIdHero(id)
                .map((item)-> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(code=HttpStatus.CREATED)
    public Mono<Heroes> createHero (@RequestBody Heroes heroes){
        log.info("a new hero was created");
        return heroesService.save(heroes);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Mono<HttpStatus> deleteByIdHero (@PathVariable String id){
        heroesService.deleteByIdHero(id);
        log.info("deleting a hero with id {}",id);
        return Mono.just(HttpStatus.NO_CONTENT);
    }
}
