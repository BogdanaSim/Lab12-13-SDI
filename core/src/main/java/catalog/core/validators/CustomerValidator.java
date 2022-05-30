package catalog.core.validators;

import catalog.core.exceptions.ValidatorException;
import catalog.core.model.Customer;

public class CustomerValidator implements Validator<Customer> {
    /**
     * Validates a Customer name
     *
     * A Customer name is valid if it's made out of letters or dashes
     *
     * @param customerName the name of the Customer to be validated
     * @throws ValidatorException on validation fail
     */
    public static void validateName(String customerName) throws ValidatorException {
        if (customerName.matches("[a-zA-Z-]+"))
            return;

        throw new ValidatorException("Invalid customer name!");
    }

    /**
     * Validates a Customer phone number
     *
     * A Customer name is valid if it's made out of digits
     *
     * @param phoneNumber the phone number of the Customer to be validated
     * @throws ValidatorException on validation fail
     */
    public static void validatePhone(String phoneNumber) throws ValidatorException {
        if (phoneNumber.matches("^+[0-9]+"))
            return;

        throw new ValidatorException("Invalid phone number!");
    }

    /**
     * Validates a Customer instance
     *
     * A Customer is valid if its name is made out of alphabetical letters (or dashes)
     * and its phone number is composed of digits.
     *
     * @param entity the Customer to be validated
     * @throws ValidatorException on validation fail
     */
    @Override
    public void validate(Customer entity) throws ValidatorException {
        validateName(entity.getName());
        validatePhone(entity.getPhoneNumber());
    }
}