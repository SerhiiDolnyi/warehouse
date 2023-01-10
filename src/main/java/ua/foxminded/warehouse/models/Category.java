package ua.foxminded.warehouse.models;

public enum Category {
    BOOK("book"),
    TV("TV"),
    SMARTPHONE("SmartPhone"),
    NOTEBOOK("Notebook");

    private final String displayValue;

    private Category(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
