package catalog.core.service;

import catalog.core.model.Preference;

import java.util.List;
import java.util.Set;

public interface IPreferenceService {
    List<Preference> getAllPreferences();

    Preference savePreference(Preference entity);

    Preference updatePreference(Preference entity);

    void deletePreference(Long id);

    Set<Preference> filterPreferencesByType(String filterString);
    Double getPercentageProduct_id(Long productId);

}
