package ua.foxminded.warehouse.models;

public enum Stage {
    REGISTERED("Registered"),
    FULFILLED("Fulfilled");

    private final String displayValue;

    private Stage(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
