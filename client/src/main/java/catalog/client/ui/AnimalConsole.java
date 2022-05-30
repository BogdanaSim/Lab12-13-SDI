package catalog.client.ui;


import catalog.core.exceptions.RepositoryException;
import catalog.core.exceptions.ValidatorException;
import catalog.core.model.Customer;
import catalog.core.validators.AnimalValidator;
import catalog.web.dto.AnimalDto;
import catalog.web.dto.AnimalsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import catalog.core.model.Animal;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class AnimalConsole extends BaseConsole {
    public static final Logger log = LoggerFactory.getLogger(AnimalConsole.class);
    @Autowired
    ExecutorService executorService;
    @Autowired
    RestTemplate restTemplate;

    protected HashMap<String, Method> functionalityMap;
    static final String EXIT_KEY = "0";
    static final String ADD_KEY = "1";
    static final String PRINT_KEY = "2";
    static final String REMOVE_KEY = "3";
    static final String UPDATE_KEY = "4";
    static final String FILTER_KEY = "5";
    static final String REPORT_KEY = "6";

    private AnimalValidator animalValidator;

    @PostConstruct
    public void init() {
        this.running = true;
        this.animalValidator = new AnimalValidator();
        HashMap<String, Method> functionalityMap = null;
        try {
            functionalityMap = new HashMap<>() {{
                put(EXIT_KEY, AnimalConsole.class.getMethod("exit"));
                put(ADD_KEY, AnimalConsole.class.getMethod("addAnimal"));
                put(PRINT_KEY, AnimalConsole.class.getMethod("printAllAnimals"));
                put(REMOVE_KEY, AnimalConsole.class.getMethod("removeAnimal"));
                put(UPDATE_KEY, AnimalConsole.class.getMethod("updateAnimal"));
                put(FILTER_KEY, AnimalConsole.class.getMethod("filterAnimals"));
                put(REPORT_KEY, AnimalConsole.class.getMethod("reportHowManyAnimalsAdopted"));
            }};
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.setFunctionalityMap(functionalityMap);
    }

    protected void printMenu() {
        System.out.println(
                "\nANIMAL SUBMENU\n" +
                        String.format("%s. Exit\n", EXIT_KEY) +
                        String.format("%s. Add\n", ADD_KEY) +
                        String.format("%s. Print all\n", PRINT_KEY) +
                        String.format("%s. Remove\n", REMOVE_KEY) +
                        String.format("%s. Update\n", UPDATE_KEY) +
                        String.format("%s. Filter by age\n", FILTER_KEY) +
                        String.format("%s. Report what percentage of animals are over 10\n", REPORT_KEY)
        );
    }

    public void runConsole() {
        String url = "http://localhost:8080/api/animals";
        try {
            AnimalsDto students = restTemplate.getForObject(url, AnimalsDto.class);
            System.out.println(students);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addAnimal() {
        log.trace("addAnimal - method entered.");
        Animal entity = this.readEntity();
        try {
            animalValidator.validate(entity);
            try {
                String url = "http://localhost:8080/api/animals";
                AnimalDto adto = new AnimalDto(entity.getCustomerID(), entity.getAge(), entity.getBreed(), entity.getColor());
                adto.setId(entity.getId());
                restTemplate.postForObject(url, adto, AnimalDto.class);


            } catch (ResourceAccessException resourceAccessException) {
                System.out.println("Inaccessible server");

            }
        } catch (ValidatorException ex) {
            System.out.println(ex.getMessage());
        }
        //return CompletableFuture.supplyAsync(() -> {

        log.trace("addAnimal - method finished");
        // },executorService);
    }

    public void printAllAnimals() {
        log.trace("printAllAnimals - method entered.");
        try {
            String url = "http://localhost:8080/api/animals";
            AnimalsDto animals = restTemplate.getForObject(url, AnimalsDto.class);
            if (animals == null)
                System.out.println("Could not retrieve animals from server");
            else {
                Set<Animal> animals1 = animals.getAnimals().stream().map(animalDto -> {
                    Animal c = new Animal(animalDto.getCustomerID(), animalDto.getAge(), animalDto.getBreed(), animalDto.getColor());
                    c.setId(animalDto.getId());
                    return c;
                }).collect(Collectors.toSet());
                for (Animal a : animals1) {
                    System.out.println(a.toString());
                }
                //System.out.println(animals.getAnimals());
            }
        } catch (ResourceAccessException resourceAccessException) {
            System.out.println("Inaccessible server");
        }
        log.trace("printAllAnimals - method finished");
    }

    public void removeAnimal() {
        log.trace("removeAnimal - method entered.");
        Long id = this.readID();
        if (id >0) {
            try {
                String url = "http://localhost:8080/api/animals";
                restTemplate.delete(url + "/{id}", id);
                System.out.println("Animal successfully deleted");
            } catch (ResourceAccessException resourceAccessException) {
                System.out.println("Inaccessible server");
            }
        } else {
            System.out.println("Invalid id!");
        }
        log.trace("removeAnimal - method finished");
    }

    public void updateAnimal() {
        boolean ok=false;
        log.trace("updateAnimal - method entered.");
        Animal entity = this.readEntity();
        try {
            animalValidator.validate(entity);
            AnimalsDto animals = restTemplate.getForObject("http://localhost:8080/api/animals", AnimalsDto.class);
            for (AnimalDto a:animals.getAnimals()){
                if (a.getId().equals(entity.getId()))
                    ok=true;
            }
            if (ok){
            try {
                String url = "http://localhost:8080/api/animals";
                AnimalDto animalToUpdate = new AnimalDto(entity.getCustomerID(), entity.getAge(), entity.getBreed(), entity.getColor());
                animalToUpdate.setId(entity.getId());
                restTemplate.put(url + "/{id}", animalToUpdate, animalToUpdate.getId());
                System.out.println("Animal successfully updated");
            } catch (ResourceAccessException resourceAccessException) {
                System.out.println("Inaccessible server");
            }catch (RepositoryException ex){
                System.out.println(ex.getMessage());
            }}else{
                System.out.println("Animal with this id does not exist!");
            }
        } catch (ValidatorException ex) {
            System.out.println(ex.getMessage());
        }
        log.trace("updateAnimal - method finished");
    }

    protected Animal readEntity() {
        System.out.println("Read animal {id, room_id, customer_id, age, breed, color}\n");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        Long id = null;

        Long idCustomer = null;
        int age = -1;
        String breed = null;
        String color = null;
        try {
            System.out.print("Animal ID: ");
            id = Long.valueOf(bufferRead.readLine());
            System.out.print("Customer ID: ");
            idCustomer = Long.valueOf(bufferRead.readLine());
            System.out.print("Animal age: ");
            age = Integer.parseInt(bufferRead.readLine());
            System.out.print("Animal breed: ");
            breed = bufferRead.readLine();
            System.out.print("Animal color: ");
            color = bufferRead.readLine();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());

            // Unrecoverable from, so exit
            this.exit();
        }

        Animal animal = new Animal(idCustomer, age, breed, color);
        animal.setId(id);

        return animal;
    }


    protected Animal createEntity(Long id) {

        Animal animal = new Animal(id, 10, "aaa", "red");
        animal.setId(id);
        return animal;
    }

    public void filterAnimals() {
        log.trace("filterAnimals - method entered.");
        String ageToFilterBy;
        long id = 1;
        //Animal animal = createEntity(id);
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.print("Animal age to filter by: ");
            ageToFilterBy = bufferRead.readLine();
            try {
                AnimalValidator.validateAge(Integer.parseInt(ageToFilterBy));

                //animal.setAge(ageToFilterBy);
//            Set<Animal> filtered = ((AnimalService) this.service).filterAnimalsByAgeLessThan(ageToFilterBy);
//            filtered.forEach(System.out::println);
                try {
                    String url = "http://localhost:8080/api/animals";
                    AnimalsDto animals = restTemplate.getForObject(url + "/" + ageToFilterBy, AnimalsDto.class);
                    if (animals == null)
                        System.out.println("Could not retrieve animals from server");
                    else {
                        Set<Animal> animals1 = animals.getAnimals().stream().map(animalDto -> {
                            Animal c = new Animal(animalDto.getCustomerID(), animalDto.getAge(), animalDto.getBreed(), animalDto.getColor());
                            c.setId(animalDto.getId());
                            return c;
                        }).collect(Collectors.toSet());
                        for (Animal a : animals1) {
                            System.out.println(a.toString());
                        }
                        //System.out.println(animals.getAnimals());
                    }
                } catch (ResourceAccessException resourceAccessException) {
                    System.out.println("Inaccessible server");
                }
            } catch (ValidatorException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());

            // Unrecoverable from, so exit
            this.exit();
        }
        log.trace("filterAnimals - method finished.");
    }

    public void reportHowManyAnimalsAdopted() {
        log.trace("reportHowManyAnimalsAdopted - method entered.");
        try {
            String url = "http://localhost:8080/api/animals";
            Integer animals = restTemplate.getForObject(url + "/report", Integer.class);
            if (animals == null)
                System.out.println("Could not retrieve report from server");
            else {
                System.out.println(animals + "% of animals are over 10 years old.");
                //System.out.println(animals);
            }
        } catch (ResourceAccessException resourceAccessException) {
            System.out.println("Inaccessible server");
        }
        log.trace("reportHowManyAnimalsAdopted - method finished.");
//        int percentage = ((AnimalService) this.service).reportHowManyAnimalsAdopted();
//        System.out.print(percentage);
//        System.out.println("%");
    }
}
