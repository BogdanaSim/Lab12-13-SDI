package catalog.core.service;

import catalog.core.model.Product;

import java.util.List;
import java.util.Set;

public interface IProductService {
    List<Product> getAllProducts();

    Product saveProduct(Product entity);

    Product updateProduct(Product entity);

    void deleteProduct(Long id);

    Set<Product> filterProductsByType(String filterString);

}
