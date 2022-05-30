package catalog.web.converter;

import catalog.core.model.Preference;
import catalog.web.dto.PreferenceDto;
import org.springframework.stereotype.Component;

@Component
public class PreferenceConverter extends BaseConverter<Preference, PreferenceDto> {
    @Override
    public Preference convertDtoToModel(PreferenceDto dto) {
        Preference model = new Preference();
        model.setId(dto.getId());
        model.setAnimal_id(dto.getAnimal_id());
        model.setProduct_id(dto.getProduct_id());
        model.setReason(dto.getReason());
        return model;
    }

    @Override
    public PreferenceDto convertModelToDto(Preference preference) {
        PreferenceDto dto = new PreferenceDto(preference.getAnimal_id(), preference.getProduct_id(), preference.getReason());
        dto.setId(preference.getId());
        return dto;
    }
}
