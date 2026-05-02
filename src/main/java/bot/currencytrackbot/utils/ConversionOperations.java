package bot.currencytrackbot.utils;

public enum ConversionOperations {
    BUY("Покупка"),
    SELL("Продажа");

    private final String operation;

    ConversionOperations(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
