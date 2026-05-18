package io.binarycodes.vaadin.durationpicker;


public final class Util {
    private Util() {
        // do not instantiate
    }

    public static boolean isBlank(final CharSequence cs) {
        if (cs == null || cs.isEmpty()) {
            return true;
        }

        return cs.chars().allMatch(Character::isWhitespace);
    }

}
