package catalog.core.service;

import catalog.core.exceptions.RepositoryException;
import catalog.core.model.Animal;
import catalog.core.model.Customer;
import catalog.core.model.Product;
import catalog.core.repository.AnimalRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Service("animalService")
public class AnimalService implements IAnimalService {
    public static final Logger log = LoggerFactory.getLogger(AnimalService.class);

    @Autowired
    private AnimalRepository animalRepository;


    /**
     * Returns all animals whose age are under a given number.
     *
     * @param filterAge age (int number) to filter animals by
     * @return animals whose age are less than the given int number
     */
    public Set<Animal> filterAnimalsByAge(Integer filterAge) {
        log.trace("filterAnimalsByAge - method entered: filterAge={}", filterAge);
        List<Animal> entities = animalRepository.findAnimalByAgeIsLessThanEqualOrderByAgeAsc(filterAge);
//
        Set<Animal> res=new HashSet<>(entities);
//        Iterable<Animal> entities=this.getAllAnimals();
//        Set<Animal> filteredAnimals = new HashSet<>();
//        entities.forEach(filteredAnimals::add);
//        filteredAnimals.removeIf(entity -> entity.getAge() > filterAge);
        log.trace("filterAnimalsByAge - method finished");
        //return (Set<Animal>) entities;
        return res;
    }

    public int reportHowManyAnimalsAdoptedAreOver10() {
        log.trace("reportHowManyAnimalsAdoptedAreOver10 --- method entered");
        Iterable<Animal> entities = animalRepository.findAll();
        Set<Animal> adoptedAnimals = new HashSet<>();
        entities.forEach(adoptedAnimals::add);
        float totalAnimalsNumber = adoptedAnimals.size();
        adoptedAnimals.removeIf(entity -> entity.getAge() >= 10);
        float adoptedAnimalsNumber = adoptedAnimals.size();
        log.trace("reportHowManyAnimalsAdoptedAreOver10 - method finished");
        return Math.round(adoptedAnimalsNumber / totalAnimalsNumber * 100);
    }

    @Override
    public List<Animal> getAllAnimals() {
        log.trace("getAllAnimals --- method entered");

        List<Animal> result = animalRepository.findAll();

        log.trace("getAllAnimals - method finished: result={}", result);

        return result;
    }

    @Override
    public Animal saveAnimal(Animal entity) {
        //AtomicReference<Animal> a = null;
        boolean found = false;
        Animal res=entity;
        log.trace("saveAnimal - method entered: animal={}", entity);
////        animalRepository.findById(entity.getId()).ifPresentOrElse(animal -> {
////            log.debug("saveAnimal - failed since animal with id " + entity.getId() + " already exists!");
////        },()-> {
////            //assert false;
////            a.set(animalRepository.save(entity));
////        });
////        //Animal a=animalRepository.save(entity);
//        List<Animal> animals = this.animalRepository.findAll();
//        for (Animal animal : animals) {
//            if (animal.getId().equals(entity.getId())) {
//                log.debug("saveAnimal - failed since animal with id " + entity.getId() + " already exists!");
//                found = true;
//            }
//        }
//        if (!found) {
//
//            res= this.animalRepository.save(entity);
//
//        }
        long id = 0;
        for (Animal animal : this.animalRepository.findAll())
            id = Math.max(id, animal.getId() + 1);
        Animal animalToBeAdded = new Animal(entity.getCustomerID(),entity.getAge(),entity.getBreed(),entity.getColor());
        animalToBeAdded.setId(id);
        animalRepository.save(animalToBeAdded);
        log.trace("saveAnimal - method finished");
        return res;
    }

    @Override
    @Transactional
    public Animal updateAnimal(Animal entity) {
        log.trace("updateAnimal - method entered: animal={}", entity);

        animalRepository.findById(entity.getId())
                .ifPresentOrElse(a -> {
                    a.setAge(entity.getAge());
                    a.setBreed(entity.getBreed());
                    a.setColor(entity.getColor());
                    a.setCustomerID(entity.getCustomerID());
                    log.debug("updateAnimal - updated: animal={}", a);
                },()->{
                    throw new RepositoryException("Id does not exist!");
                });

        log.trace("updateAnimal - method finished");

        return animalRepository.findById(entity.getId()).get();
    }


    @Override
    public void deleteAnimal(Long id) {
        log.trace("deleteAnimal - method entered: id={}", id);
        animalRepository.deleteById(id);
        log.trace("deleteAnimal - method finished");
    }
}
