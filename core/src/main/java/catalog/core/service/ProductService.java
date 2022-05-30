package catalog.core.service;

import catalog.core.model.Customer;
import catalog.core.model.Preference;
import catalog.core.model.Product;
import catalog.core.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class ProductService implements IProductService {
    public static final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;


    /**
     * Returns all products whose type contain the given string.
     *
     * @param filterString string to filter types by
     * @return products whose type contain the given string
     */
    public Set<Product> filterProductsByType(String filterString) {
        log.trace("filterProductsByType - method entered: filterType={}", filterString);

//        Iterable<Product> entities = productRepository.findAll();
//
//        Set<Product> filteredProducts = new HashSet<>();
//        entities.forEach(filteredProducts::add);
//        filteredProducts.removeIf(entity -> !entity.getType().contains(filterString));
        List<Product> filteredProducts=productRepository.findProductByTypeContains(filterString);
        Set<Product> res=new HashSet<>(filteredProducts);
        log.trace("filterProductsByType - method finished");

        return res;
    }

    @Override
    public List<Product> getAllProducts() {
        log.trace("getAllProducts --- method entered");

        List<Product> result = productRepository.findAll();

        log.trace("getAllProducts: result={}", result);

        return result;
    }

    @Override
    public Product saveProduct(Product entity) {
        //AtomicReference<Product> a = null;
        boolean found = false;
        Product res=entity;
        log.trace("saveProduct - method entered: product={}", entity);
//        productRepository.findById(entity.getId()).ifPresentOrElse(animal -> {
//            log.debug("savePreference - failed since preference with id " + entity.getId() + " already exists!");
//        },()-> {
//            //assert false;
//            a.set(productRepository.save(entity));
//        });
//        List<Product> products = this.productRepository.findAll();
//        for (Product product : products) {
//            if (product.getId().equals(entity.getId())) {
//                log.debug("saveProduct - failed since product with id " + entity.getId() + " already exists!");
//                found = true;
//            }
//        }
//        if (!found) {
//
//            res= this.productRepository.save(entity);
//
//        }
        long id = 0;
        for (Product product : this.productRepository.findAll())
            id = Math.max(id, product.getId() + 1);
        Product productToBeAdded = new Product(entity.getType(),entity.getPrice());
        productToBeAdded.setId(id);
        productRepository.save(productToBeAdded);

        log.trace("saveProduct - method finished");
      //  return productRepository.save(entity);
        return res;
    }

    @Override
    @Transactional
    public Product updateProduct(Product entity) {
        log.trace("updateProduct - method entered: product={}", entity);

        productRepository.findById(entity.getId())
                .ifPresent(a -> {
                    a.setPrice(entity.getPrice());
                    a.setType(entity.getType());
                    log.debug("updateProduct - updated: product={}", a);
                });

        log.trace("updateProduct - method finished");
        return productRepository.findById(entity.getId()).get();
    }

    @Override
    public void deleteProduct(Long id) {
        log.trace("deleteProduct - method entered: id={}", id);
        productRepository.deleteById(id);
        log.trace("deleteProduct - method finished");
    }
}
