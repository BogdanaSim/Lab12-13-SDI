package catalog.web.controller;

import catalog.core.exceptions.RepositoryException;
import catalog.core.model.Animal;
import catalog.core.service.AnimalService;
import catalog.core.service.IAnimalService;
import catalog.web.converter.AnimalConverter;
import catalog.web.dto.AnimalDto;
import catalog.web.dto.AnimalsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AnimalController {
    public static final Logger log = LoggerFactory.getLogger(AnimalController.class);

    @Autowired
    private IAnimalService animalService;

    @Autowired
    private AnimalConverter animalConverter;

    @GetMapping(value = "/testn")
    public String checkTest() {
        return "test";
    }

    @RequestMapping(value = "/animal")
    public String getHello() {
        return "hello animal!!";
    }

    @RequestMapping(value = "/animals")
    AnimalsDto getAllAnimals() {
        log.trace("getAllAnimals --- method entered");
        AnimalsDto a=new AnimalsDto( animalConverter.convertModelsToDtos(
                animalService.getAllAnimals()));
//        return new AnimalsDto(
//                animalConverter.convertModelsToDtos(
//                        animalService.getAllAnimals()));
        log.trace("getAllAnimals: result={}", a);
        return a;
    }


    @RequestMapping(value = "/animals/{age}")
    AnimalsDto filterAnimals(@PathVariable String age) {
        log.trace("filterAnimals --- method entered, age = {}",age);
        AnimalsDto a=new AnimalsDto(animalConverter.convertModelsToDtos(
                animalService.filterAnimalsByAge(Integer.parseInt(age))));
//        return new AnimalsDto(
//                animalConverter.convertModelsToDtos(
//                        animalService.filterAnimalsByAge(Integer.parseInt(age))));
        log.trace("filterAnimals - method finished: result={}", a);
        return a;
    }

    @RequestMapping(value = "/animals/report")
    Integer getReportAnimals() {
        log.trace("getReportAnimals --- method entered");
        AnimalDto an = new AnimalDto();
        int res=animalService.reportHowManyAnimalsAdoptedAreOver10();
        log.trace("getReportAnimals - method finished: result={}", res);
        //return animalService.reportHowManyAnimalsAdoptedAreOver10();
        return res;

    }


    @RequestMapping(value = "/animals", method = RequestMethod.POST)
    AnimalDto addAnimal(@RequestBody AnimalDto animalDto) {
        log.trace("addAnimal - method entered: animal={}", animalDto);
        Animal animal = animalConverter.convertDtoToModel(animalDto);

        Animal result = animalService.saveAnimal(animal);

        log.trace("addAnimal - method finished: result ={}",result);
        return animalConverter.convertModelToDto(result);
    }

    @RequestMapping(value = "/animals/{id}", method = RequestMethod.PUT)
    AnimalDto updateAnimal(@PathVariable Long id,
                           @RequestBody AnimalDto dto) throws RepositoryException {
        log.trace("updateAnimal - method entered: animal={}", dto);
        Long getId=id;
        AnimalDto a=animalConverter.convertModelToDto(
                animalService.updateAnimal(
                        animalConverter.convertDtoToModel(dto)
                ));
        log.trace("updateAnimal - method finished: result ={}",a);
//        return
//                animalConverter.convertModelToDto(
//                        animalService.updateAnimal(
//                                animalConverter.convertDtoToModel(dto)
//                        ));
        return a;
    }

    @RequestMapping(value = "/animals/{id}", method = RequestMethod.DELETE)
    ResponseEntity<?> deleteAnimal(@PathVariable Long id) {
        log.trace("deleteAnimal - method entered: id={}", id);
        animalService.deleteAnimal(id);
        log.trace("deleteAnimal - method finished");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

