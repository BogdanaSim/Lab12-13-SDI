package catalog.web.converter;

import catalog.core.model.Animal;
import catalog.web.dto.AnimalDto;
import org.springframework.stereotype.Component;

@Component
public class AnimalConverter extends BaseConverter<Animal,AnimalDto> {
    @Override
    public Animal convertDtoToModel(AnimalDto dto) {
        Animal model = new Animal();
        model.setId(dto.getId());
        model.setCustomerID(dto.getCustomerID());
        model.setAge(dto.getAge());
        model.setColor(dto.getColor());
        model.setBreed(dto.getBreed());
        return model;
    }

    @Override
    public AnimalDto convertModelToDto(Animal animal) {
        AnimalDto dto = new AnimalDto(animal.getCustomerID(), animal.getAge(), animal.getBreed(), animal.getColor());
        dto.setId(animal.getId());
        return dto;
    }
}
