package catalog.core.validators;

import catalog.core.exceptions.ValidatorException;
import catalog.core.model.Product;

public class ProductValidator implements Validator<Product> {
    /**
     * Validates if the type contains only letters
     *
     * @param type is the type of the product
     * @throws ValidatorException if it doesn't meet the criteria
     */
    public static void validateType(String type) throws ValidatorException {
        if (type.matches("[a-zA-Z-]+"))
            return;

        throw new ValidatorException("Invalid type!");
    }

    /**
     * Validates if the price is a number above 0
     *
     * @param price is the price of the product
     * @throws ValidatorException if it doesn't meet the criteria
     */
    public static void validatePrice(Float price) throws ValidatorException {
        if (price >= 0)
            return;

        throw new ValidatorException("Invalid price!");
    }

    /**
     * Validates the product (type, price)
     *
     * @param entity is the product
     * @throws ValidatorException if it doesn't meet the criteria
     */

    @Override
    public void validate(Product entity) throws ValidatorException {
        validateType(entity.getType());
        validatePrice(entity.getPrice());
    }
}