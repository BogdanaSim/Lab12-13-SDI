package catalog.core.repository;

import catalog.core.model.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
@NoRepositoryBean
public interface CatalogRepository <T extends BaseEntity<ID>, ID extends Serializable>
        extends JpaRepository<T, ID> {
}
