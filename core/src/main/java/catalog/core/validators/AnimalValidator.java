package catalog.core.validators;

import catalog.core.exceptions.ValidatorException;
import catalog.core.model.Animal;

public class AnimalValidator implements Validator<Animal> {
    /**
     * Validates if the breed contains only letters
     *
     * @param breed is the breed of the animal
     * @throws ValidatorException if it doesn't meet the criteria
     */
    public static void validateBreed(String breed) throws ValidatorException {
        if (breed.matches("[a-zA-Z- ]+"))
            return;

        throw new ValidatorException("Invalid breed!");
    }

    /**
     * Validates if the color contains only letters
     *
     * @param color is the color of the animal
     * @throws ValidatorException if it doesn't meet the criteria
     */
    public static void validateColor(String color) throws ValidatorException {
        if (color.matches("[a-zA-Z- ]+"))
            return;

        throw new ValidatorException("Invalid color!");
    }

    /**
     * Validates if the age is a number between 0 and 20
     *
     * @param age is the age of the animal
     * @throws ValidatorException if it doesn't meet the criteria
     */
    public static void validateAge(int age) throws ValidatorException {
        if (age >= 0 && age <= 20)
            return;

        throw new ValidatorException("Invalid age!");
    }

    /**
     * Validates the animal (age, color, breed)
     *
     * @param entity is the animal
     * @throws ValidatorException if it doesn't meet the criteria
     */
    @Override
    public void validate(Animal entity) throws ValidatorException {
        validateAge(entity.getAge());
        validateColor(entity.getColor());
        validateBreed(entity.getBreed());
    }
}
