package catalog.core.validators;

import catalog.core.exceptions.ValidatorException;
import catalog.core.model.Preference;

public class PreferenceValidator implements Validator<Preference> {
    /**
     * Validates if the reason contains only letters
     *
     * @param reason is the reason of the preference
     * @throws ValidatorException if it doesn't meet the criteria
     */
    public static void validateReason(String reason) throws ValidatorException {
        if (reason.matches("[a-zA-Z- ]+"))
            return;

        throw new ValidatorException("Invalid reason!");
    }

    /**
     * Validates the preference (reason)
     *
     * @param entity is the preference
     * @throws ValidatorException if it doesn't meet the criteria
     */

    @Override
    public void validate(Preference entity) throws ValidatorException {
        validateReason(entity.getReason());
    }
}