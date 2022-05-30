package catalog.core.service;

import catalog.core.model.Animal;
import catalog.core.model.Preference;
import catalog.core.model.Preference;
import catalog.core.repository.PreferenceRepository;
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
public class PreferenceService implements IPreferenceService {
    public static final Logger log = LoggerFactory.getLogger(PreferenceService.class);

    @Autowired
    private PreferenceRepository preferenceRepository;

    /**
     * Returns all preferences whose reason contain the given string.
     *
     * @param filterString string to filter types by
     * @return preferences whose reason contain the given string
     */
    public Set<Preference> filterPreferencesByType(String filterString) {
        log.trace("filterPreferencesByType - method entered: filterType={}", filterString);
//        Iterable<Preference> entities = preferenceRepository.findAll();
//
//        Set<Preference> filteredPreferences = new HashSet<>();
//        entities.forEach(filteredPreferences::add);
//        filteredPreferences.removeIf(entity -> !entity.getReason().contains(filterString));
        List<Preference> filteredPreferences=preferenceRepository.findPreferenceByReasonContains(filterString);
        Set<Preference> res=new HashSet<>(filteredPreferences);
        log.trace("filterPreferencesByType - method finished");
        return res;
    }

    public Double getPercentageProduct_id(Long productId) {
        log.trace("getPercentageProduct_id - method entered: productId={}", productId);
        List<Preference> allPreferences = this.getAllPreferences();
        Double totalPreferences = (double) allPreferences.size();
        Double productIdPreferences = 0.0;
        for (Preference preference : allPreferences) {
            if (preference.getProduct_id().equals(productId)) {
                productIdPreferences++;
            }
        }
        if(totalPreferences.equals(0.0))
        {
            log.trace("getPercentageProduct_id - method finished");
            return (double) 0;
        }
        log.trace("getPercentageProduct_id - method finished");
        return productIdPreferences / totalPreferences * 100.0;
    }

    @Override
    public List<Preference> getAllPreferences() {
        log.trace("getAllPreferences --- method entered");

        List<Preference> result = preferenceRepository.findAll();

        log.trace("getAllPreferences: result={}", result);

        return result;
    }

    @Override
    public Preference savePreference(Preference entity) {
       // AtomicReference<Preference> a = null;
        boolean found = false;
        Preference res=entity;
        log.trace("savePreference - method entered: preference={}", entity);
//        preferenceRepository.findById(entity.getId()).ifPresentOrElse(animal -> {
//            log.debug("savePreference - failed since preference with id " + entity.getId() + " already exists!");
//        },()-> {
//            assert false;
//            a.set(preferenceRepository.save(entity));
//        });
//        List<Preference> preferences = this.preferenceRepository.findAll();
//        for (Preference preference : preferences) {
//            if (preference.getId().equals(entity.getId())) {
//                log.debug("savePreference - failed since preference with id " + entity.getId() + " already exists!");
//                found = true;
//            }
//        }
//        if (!found) {
//
//            res= this.preferenceRepository.save(entity);
//
//        }
        long id = 0;
        for (Preference preference : this.preferenceRepository.findAll())
            id = Math.max(id, preference.getId() + 1);
        Preference preferenceToBeAdded = new Preference(entity.getAnimal_id(),entity.getProduct_id(), entity.getReason());
        preferenceToBeAdded.setId(id);
        preferenceRepository.save(preferenceToBeAdded);
        log.trace("savePreference - method finished");
        //return preferenceRepository.save(entity);
        return res;
    }

    @Override
    @Transactional
    public Preference updatePreference(Preference entity) {
        log.trace("updatePreference - method entered: preference={}", entity);

        preferenceRepository.findById(entity.getId())
                .ifPresent(a -> {
                    a.setAnimal_id(entity.getAnimal_id());
                    a.setProduct_id(entity.getProduct_id());
                    a.setReason(entity.getReason());
                    log.debug("updatePreference - updated: preference={}", a);
                });

        log.trace("updatePreference - method finished");
        return preferenceRepository.findById(entity.getId()).get();
    }

    @Override
    public void deletePreference(Long id) {
        log.trace("deletePreference - method entered: id={}", id);
        preferenceRepository.deleteById(id);
        log.trace("deletePreference - method finished");
    }
}
