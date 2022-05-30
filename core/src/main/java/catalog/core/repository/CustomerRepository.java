package catalog.core.repository;

import catalog.core.model.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CatalogRepository<Customer, Long> {

    List<Customer> findByNameContainingOrderByNameAsc(@Param("filterString") String filterString);

    List<Customer> findAllByOrderByNameAsc();
}
