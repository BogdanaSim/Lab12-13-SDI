package catalog.core.repository;

import catalog.core.model.Preference;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceRepository extends CatalogRepository<Preference, Long> {
    List<Preference> findPreferenceByReasonContains(@Param("filterString") String filterString);
}
