package catalog.core.service;

import catalog.core.model.Animal;

import java.util.List;
import java.util.Set;

public interface IAnimalService {
    List<Animal> getAllAnimals();

    Animal saveAnimal(Animal entity);

    Animal updateAnimal(Animal entity);

    void deleteAnimal(Long id);

    Set<Animal> filterAnimalsByAge(Integer filterAge);

    int reportHowManyAnimalsAdoptedAreOver10();
}
