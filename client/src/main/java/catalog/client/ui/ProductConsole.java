package catalog.client.ui;

import catalog.core.exceptions.ValidatorException;
import catalog.core.model.Preference;
import catalog.core.model.Product;
import catalog.core.validators.ProductValidator;
import catalog.web.dto.AnimalsDto;
import catalog.web.dto.ProductDto;
import catalog.web.dto.ProductsDto;
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
public class ProductConsole extends BaseConsole {
    public static final Logger log = LoggerFactory.getLogger(ProductConsole.class);

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

    private ProductValidator productValidator;

    @PostConstruct
    public void init() {

        this.productValidator = new ProductValidator();
        HashMap<String, Method> functionalityMap = null;
        try {
            functionalityMap = new HashMap<>() {{
                put(EXIT_KEY, ProductConsole.class.getMethod("exit"));
                put(ADD_KEY, ProductConsole.class.getMethod("addProduct"));
                put(PRINT_KEY, ProductConsole.class.getMethod("printAllProducts"));
                put(REMOVE_KEY, ProductConsole.class.getMethod("removeProduct"));
                put(UPDATE_KEY, ProductConsole.class.getMethod("updateProduct"));
                put(FILTER_KEY, ProductConsole.class.getMethod("filterProducts"));
            }};
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        this.setFunctionalityMap(functionalityMap);
    }


    @Override
    protected void printMenu() {
        System.out.println(
                "\nPRODUCT SUBMENU\n" +
                        String.format("%s. Exit\n", EXIT_KEY) +
                        String.format("%s. Add\n", ADD_KEY) +
                        String.format("%s. Print all\n", PRINT_KEY) +
                        String.format("%s. Remove\n", REMOVE_KEY) +
                        String.format("%s. Update\n", UPDATE_KEY) +
                        String.format("%s. Filter by type\n", FILTER_KEY)
        );
    }

    public void addProduct() {
        log.trace("addProduct - method entered.");
        Product entity = this.readEntity();
        //return CompletableFuture.supplyAsync(() -> {
        try {
            productValidator.validate(entity);
            try {
                String url = "http://localhost:8080/api/products";
                ProductDto adto = new ProductDto(entity.getType(), entity.getPrice());
                adto.setId(entity.getId());
                restTemplate.postForObject(url, adto, ProductDto.class);
                System.out.println("Product successfully updated");


            } catch (ResourceAccessException resourceAccessException) {
                System.out.println("Inaccessible server");
            }
        } catch (ValidatorException ex) {
            System.out.println(ex.getMessage());
        }
        log.trace("addProduct - method finished");
        // },executorService);
    }

    public void printAllProducts() {
        log.trace("printAllProducts - method entered.");
        try {
            String url = "http://localhost:8080/api/products";
            ProductsDto products = restTemplate.getForObject(url, ProductsDto.class);
            if (products == null)
                System.out.println("Could not retrieve products from server");
            else {
                Set<Product> products1 = products.getProducts().stream().map(productDto -> {
                    Product c = new Product(productDto.getType(), productDto.getPrice());
                    return c;
                }).collect(Collectors.toSet());
                for (Product p : products1) {
                    System.out.println(p.toString());
                }
                //System.out.println(products.getProducts());
            }
        } catch (ResourceAccessException resourceAccessException) {
            System.out.println("Inaccessible server");
        }
        log.trace("printAllProducts - method finished");

    }

    public void removeProduct() {
        log.trace("removeProduct - method entered.");
        Long id = this.readID();
        if (id > 0) {
            try {
                String url = "http://localhost:8080/api/products";
                restTemplate.delete(url + "/{id}", id);
                System.out.println("Product successfully deleted");
            } catch (ResourceAccessException resourceAccessException) {
                System.out.println("Inaccessible server");
            }
        } else {
            System.out.println("Invalid id!");
        }
        log.trace("removeProduct- method finished");
    }

    public void updateProduct() {
        log.trace("updateProduct - method entered.");
        Product entity = this.readEntity();
        try {
            productValidator.validate(entity);
            try {
                String url = "http://localhost:8080/api/products";
                ProductDto productToUpdate = new ProductDto(entity.getType(), entity.getPrice());
                productToUpdate.setId(entity.getId());
                restTemplate.put(url + "/{id}", productToUpdate, productToUpdate.getId());
                System.out.println("Product successfully updated");
            } catch (ResourceAccessException resourceAccessException) {
                System.out.println("Inaccessible server");
            }
        } catch (ValidatorException ex) {
            System.out.println(ex.getMessage());
        }
        log.trace("updateProduct - method finished");
    }

    protected Product readEntity() {
        System.out.println("Read product {id, type, price}\n");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        Long id = null;
        String type = null;
        Float price = null;
        try {
            System.out.print("Product ID: ");
            id = Long.valueOf(bufferRead.readLine());
            System.out.print("Product Type: ");
            type = bufferRead.readLine();
            System.out.print("Product Price: ");
            price = Float.parseFloat(bufferRead.readLine());
        } catch (IOException exception) {
            System.out.println(exception.getMessage());

            // Unrecoverable from, so exit
            this.exit();
        }

        Product product = new Product(type, price);
        product.setId(id);

        return product;
    }

    public void filterProducts() {
        log.trace("filterProducts- method entered.");
        String type = "";
        long id = 1;

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Filter By Type: ");
        try {
            type = bufferRead.readLine();
            try {
                ProductValidator.validateType(type);
                try {
                    String url = "http://localhost:8080/api/products";
                    ProductsDto products = restTemplate.getForObject(url + "/" + type, ProductsDto.class);
                    if (products == null)
                        System.out.println("Could not retrieve products from server");
                    else {
                        Set<Product> products1 = products.getProducts().stream().map(productDto -> {
                            Product c = new Product(productDto.getType(), productDto.getPrice());
                            c.setId(productDto.getId());
                            return c;
                        }).collect(Collectors.toSet());
                        for (Product p : products1) {
                            System.out.println(p.toString());
                        }
                        // System.out.println(products.getProducts());
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
        log.trace("filterProducts - method finished.");
//        Set<Product> filtered = ((ProductService) this.service).filterProductsByType(type);
//        filtered.forEach(System.out::println);
    }
}
