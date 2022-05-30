package catalog.client.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
@Service
public class MasterConsole extends BaseConsole {
    public static final String EXIT_KEY = "0";
    public static final String CUSTOMER_KEY = "1";
    public static final String PREFERENCE_KEY = "2";

    public static final String ANIMAL_KEY = "3";
    public static final String PRODUCT_KEY = "4";

    @Autowired
    private  CustomerConsole customerConsole;
    @Autowired
    private  PreferenceConsole preferenceConsole;
    @Autowired
    private  ProductConsole productConsole;
    @Autowired
    private  AnimalConsole animalConsole;

    @PostConstruct
    public void init() {


        HashMap<String, Method> functionalityMap = null;
        try {
            functionalityMap = new HashMap<>() {{
                put(EXIT_KEY, MasterConsole.class.getMethod("exit"));
                put(CUSTOMER_KEY, MasterConsole.class.getMethod("runCustomerMenu"));
                put(PREFERENCE_KEY, MasterConsole.class.getMethod("runPreferenceMenu"));
                put(ANIMAL_KEY, MasterConsole.class.getMethod("runAnimalMenu"));
                put(PRODUCT_KEY, MasterConsole.class.getMethod("runProductMenu"));

            }};
        } catch (NoSuchMethodException e) {
            System.out.println(e.getMessage());

            this.exit();
        }

        this.setFunctionalityMap(functionalityMap);
    }


    @Override
    public void printMenu() {
        System.out.println("\nSHELTER\n" +
                String.format("%s. Exit\n", EXIT_KEY) +
                String.format("%s. Customer submenu\n", CUSTOMER_KEY) +
                String.format("%s. Preference submenu\n", PREFERENCE_KEY) +
                String.format("%s. Animal submenu\n", ANIMAL_KEY) +
                String.format("%s. Product submenu\n", PRODUCT_KEY)

        );
    }

    public void runCustomerMenu() {
        this.runInnerConsole(this.customerConsole);
    }

    public void runPreferenceMenu() {
        this.runInnerConsole(this.preferenceConsole);
    }


    public void runAnimalMenu() {
        this.runInnerConsole(this.animalConsole);
    }

    public void runProductMenu() {
        this.runInnerConsole(this.productConsole);
    }


}
