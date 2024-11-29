package zhaw.voter.util;

public class InputValidator {

    private InputValidator() {
    }

    public static void validateInput(String input, String errorMsg) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMsg);
        }
    }
}
