package bot.currencytrackbot.utils;

public enum Capability {
    EXCHANGE_RATE("Курс валют"),
    CONVERT_VALUE("Конвертация");

    private final String operation;

    Capability(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
