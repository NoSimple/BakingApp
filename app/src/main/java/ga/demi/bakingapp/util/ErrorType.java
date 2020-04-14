package ga.demi.bakingapp.util;

public enum ErrorType {

    EMPTY_DATA("No data available"),
    NETWORK_CONNECTION("No network connection");

    private String errorName;

    ErrorType(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorName() {
        return errorName;
    }
}