package catalog.core.repository;

import catalog.core.model.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends CatalogRepository<Animal, Long> {
//    List<Animal> findAnimalByAgeIsLessThan(Integer age);
//
//    Page<Animal> findAnimalByBreedOrderByBreedAsc(String breed);
    List<Animal> findAnimalByAgeIsLessThanEqualOrderByAgeAsc(Integer age);

}
