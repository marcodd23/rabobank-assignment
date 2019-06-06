package io.sytac.rabobank.app.model;

public enum Irregularities {
    BALANCE_NOT_VALID("The final balance is not correct"),
    DUPLICATED_REF_NUMB("The reference number is Duplicated");

    private String cause;

    Irregularities(String cause) {
        this.cause = cause;
    }

    public String get() {
        return cause;
    }

}
