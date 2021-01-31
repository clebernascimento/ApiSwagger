package com.example.demo.resources;

import com.example.demo.models.Person;
import com.example.demo.repositories.PersonRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Api
@RestController
@RequestMapping(path = "/persons")
public class PersonResource {

    private PersonRepository personRepository;

    public PersonResource(PersonRepository personRepository){
        super();
        this.personRepository = personRepository;
    }

    @ApiOperation("Cadastra pessoas uma por vez")
    @PostMapping
    public ResponseEntity<Person> save(@RequestBody Person person){
        personRepository.save(person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @ApiOperation("Consulta pessoas, retornando todas em uma lista")
    @GetMapping
    public ResponseEntity<List<Person>> getAll(){
        List<Person> personList = new ArrayList<>();
        personList = personRepository.findAll();
        return  new ResponseEntity<>(personList, HttpStatus.OK);
    }

    @ApiOperation("Consulta pessoas, uma por vez")
    @GetMapping(path = "/{id}")
    public ResponseEntity<Optional<Person>> getById(@PathVariable Integer id){
        Optional<Person> personId;
        try {
            personId = personRepository.findById(id);
            return new ResponseEntity<Optional<Person>>(personId, HttpStatus.OK);
        }catch (NoSuchElementException exception){
            return new ResponseEntity<Optional<Person>>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("Deleta pessoas, uma por vez")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Optional<Person>> deleteById(@PathVariable Integer id){
        try {
            personRepository.deleteById(id);
            return new ResponseEntity<Optional<Person>>(HttpStatus.OK);
        }catch (NoSuchElementException exception){
            return new ResponseEntity<Optional<Person>>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation("Atualiza pessoas, uma por vez")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Person> update(@PathVariable Integer id, @RequestBody Person newPerson){
        return personRepository.findById(id)
                .map(person -> {
                    person.setName(newPerson.getName());
                    person.setAge(newPerson.getAge());
                    Person personUpdate = personRepository.save(person);
                    return ResponseEntity.ok().body(personUpdate);
                }).orElse(ResponseEntity.notFound().build());
    }
}
