package catalog.client.ui;

import catalog.core.exceptions.ValidatorException;
import catalog.core.model.Animal;
import catalog.core.model.Preference;
import catalog.core.validators.PreferenceValidator;
import catalog.web.dto.AnimalsDto;
import catalog.web.dto.PreferenceDto;
import catalog.web.dto.PreferencesDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class PreferenceConsole extends BaseConsole {
    public static final Logger log = LoggerFactory.getLogger(PreferenceConsole.class);
    @Autowired
    ExecutorService executorService;
    @Autowired
    RestTemplate restTemplate;
    static final String EXIT_KEY = "0";
    static final String ADD_KEY = "1";
    static final String PRINT_KEY = "2";
    static final String REMOVE_KEY = "3";
    static final String UPDATE_KEY = "4";
    static final String FILTER_KEY = "5";
    static final String REPORT_KEY = "6";
    private PreferenceValidator preferenceValidator;

    @PostConstruct
    public void init() {

        this.preferenceValidator = new PreferenceValidator();
        HashMap<String, Method> functionalityMap = null;
        try {
            functionalityMap = new HashMap<>() {{
                put(EXIT_KEY, PreferenceConsole.class.getMethod("exit"));
                put(ADD_KEY, PreferenceConsole.class.getMethod("addPreference"));
                put(PRINT_KEY, PreferenceConsole.class.getMethod("printAllPreferences"));
                put(REMOVE_KEY, PreferenceConsole.class.getMethod("removePreference"));
                put(UPDATE_KEY, PreferenceConsole.class.getMethod("updatePreference"));
                put(FILTER_KEY, PreferenceConsole.class.getMethod("filterPreferences"));
                put(REPORT_KEY, PreferenceConsole.class.getMethod("getPercentageByProduct"));
            }};
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.setFunctionalityMap(functionalityMap);
    }

    @Override
    public void printMenu() {
        System.out.println(
                "\nCUSTOMER SUBMENU\n" +
                        String.format("%s. Exit\n", EXIT_KEY) +
                        String.format("%s. Add\n", ADD_KEY) +
                        String.format("%s. Print all\n", PRINT_KEY) +
                        String.format("%s. Remove\n", REMOVE_KEY) +
                        String.format("%s. Update\n", UPDATE_KEY) +
                        String.format("%s. Filter by reason\n", FILTER_KEY) +
                        String.format("%s. Report for preference\n", REPORT_KEY)
        );
    }

    public void addPreference() {
        log.trace("addPreference - method entered.");
        Preference entity = this.readEntity();
        //return CompletableFuture.supplyAsync(() -> {
        try {
            preferenceValidator.validate(entity);
            try {
                String url = "http://localhost:8080/api/preferences";
                PreferenceDto adto = new PreferenceDto(entity.getAnimal_id(), entity.getProduct_id(), entity.getReason());
                adto.setId(entity.getId());
                restTemplate.postForObject(url, adto, PreferenceDto.class);

            } catch (ResourceAccessException resourceAccessException) {
                System.out.println("Inaccessible server");
            }
        } catch (ValidatorException ex) {
            System.out.println(ex.getMessage());
        }
        // },executorService);
        log.trace("addPreference - method finished");
    }

    public void printAllPreferences() {
        log.trace("printAllPreferences - method entered.");
        try {
            String url = "http://localhost:8080/api/preferences";
            PreferencesDto preferences = restTemplate.getForObject(url, PreferencesDto.class);
            if (preferences == null)
                System.out.println("Could not retrieve preferences from server");
            else {
                Set<Preference> preferences1 = preferences.getPreferences().stream().map(preferenceDto -> {
                    Preference c = new Preference(preferenceDto.getAnimal_id(), preferenceDto.getProduct_id(), preferenceDto.getReason());
                    c.setId(preferenceDto.getId());
                    return c;
                }).collect(Collectors.toSet());
                for (Preference p : preferences1) {
                    System.out.println(p.toString());
                }
                //System.out.println(preferences.getPreferences());
            }
        } catch (ResourceAccessException resourceAccessException) {
            System.out.println("Inaccessible server");
        }
        log.trace("printAllPreferences - method finished");

    }

    public void removePreference() {
        log.trace("removePreference - method entered.");

        Long id = this.readID();
        if (id > 0) {
            try {
                String url = "http://localhost:8080/api/preferences";
                restTemplate.delete(url + "/{id}", id);
                System.out.println("Preference successfully deleted");
            } catch (ResourceAccessException resourceAccessException) {
                System.out.println("Inaccessible server");
            }
        } else {
            System.out.println("Invalid id!");
        }
        log.trace("removePreference - method finished");

    }

    public void updatePreference() {
        log.trace("updatePreference - method entered.");

        Preference entity = this.readEntity();
        try {
            preferenceValidator.validate(entity);
            try {
                String url = "http://localhost:8080/api/preferences";
                PreferenceDto animalToUpdate = new PreferenceDto(entity.getAnimal_id(), entity.getProduct_id(), entity.getReason());
                animalToUpdate.setId(entity.getId());
                restTemplate.put(url + "/{id}", animalToUpdate, animalToUpdate.getId());
                System.out.println("Preference successfully updated");
            } catch (ResourceAccessException resourceAccessException) {
                System.out.println("Inaccessible server");
            }
        } catch (ValidatorException ex) {
            System.out.println(ex.getMessage());
        }
        log.trace("updatePreference - method finished");

    }

    protected Preference readEntity() {
        System.out.println("Read preference {id, animal_id, product_id, reason}\n");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        Long id = null;
        Long animalId = null;
        Long productId = null;
        String reason = null;
        try {
            System.out.print("ID: ");
            id = Long.valueOf(bufferRead.readLine());
            System.out.print("Animal ID: ");
            animalId = Long.valueOf(bufferRead.readLine());
            System.out.print("Product ID: ");
            productId = Long.valueOf(bufferRead.readLine());
            System.out.print("Reason: ");
            reason = bufferRead.readLine();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            this.exit();
        }

        Preference preference = new Preference(animalId, productId, reason);
        preference.setId(id);
        return preference;
    }

    public void filterPreferences() {
        log.trace("filterPreferences - method entered.");

        long id = 1;


        try {
            System.out.println("Filter reasons by: ");
            String filterBy = bufferedReader.readLine();
            try {
                PreferenceValidator.validateReason(filterBy);
                try {
                    String url = "http://localhost:8080/api/preferences";
                    PreferencesDto preferences = restTemplate.getForObject(url + "/" + filterBy, PreferencesDto.class);
                    if (preferences == null)
                        System.out.println("Could not retrieve preferences from server");
                    else {
                        Set<Preference> preferences1 = preferences.getPreferences().stream().map(preferenceDto -> {
                            Preference c = new Preference(preferenceDto.getAnimal_id(), preferenceDto.getProduct_id(), preferenceDto.getReason());
                            c.setId(preferenceDto.getId());
                            return c;
                        }).collect(Collectors.toSet());
                        for (Preference p : preferences1) {
                            System.out.println(p.toString());
                        }
                        //System.out.println(preferences.getAnimals());
                    }
                } catch (ResourceAccessException resourceAccessException) {
                    System.out.println("Inaccessible server");
                }
            } catch (ValidatorException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            this.exit();
        }
        log.trace("filterPreferences - method finished.");

    }

    public void getPercentageByProduct() {
        log.trace("getPercentageByProduct - method entered.");

        long id = 1;
        try {
            System.out.println("Product ID: ");
            Long productId = Long.valueOf(bufferedReader.readLine());
            if(productId>0) {
                try {
                    String url = "http://localhost:8080/api/preferences";
                    Double preferences = restTemplate.getForObject(url + "/report/"+productId,  Double.class);
                    if (preferences == null)
                        System.out.println("Could not retrieve preferences from server");
                    else {
                        System.out.println(preferences + "% of times preferences prefer this product.");
                    }
                } catch (ResourceAccessException resourceAccessException) {
                    System.out.println("Inaccessible server");
                }
            }else{
                System.out.println("Invalid id!");
            }
//            Double percentage = ((PreferenceService) this.service).getPercentageProduct_id(productId);
//            System.out.println(percentage + "% of times preferences prefer this product.");
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            this.exit();
        }
        log.trace("getPercentageByProduct - method finished.");
    }
}
