package catalog.core.repository;

import catalog.core.model.Product;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CatalogRepository<Product, Long> {
    List<Product> findProductByTypeContains( @Param("filterString") String filterString);
}
