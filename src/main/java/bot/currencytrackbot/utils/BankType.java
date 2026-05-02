package bot.currencytrackbot.utils;


import java.util.Set;

public enum  BankType {
    BELARUSBANK("Беларусбанк",
            Set.of(Capability.EXCHANGE_RATE),
            Set.of(Currency.USD, Currency.RUB, Currency.BYN, Currency.EUR)
    ),
    ALFABANK("АльфаБанк",
            Set.of(Capability.EXCHANGE_RATE, Capability.CONVERT_VALUE),
            Set.of(Currency.USD, Currency.RUB, Currency.BYN, Currency.EUR)
    );

    private final String title;
    private final Set<Capability> capabilities;
    private final Set<Currency> currencies;

    BankType(String title, Set<Capability> capabilities, Set<Currency> currencies) {
        this.title = title;
        this.capabilities = capabilities;
        this.currencies = currencies;
    }
    public String getTitle() {
        return title;
    }

    public Set<Capability> getCapabilities() {
        return capabilities;
    }

    public Set<Currency> getCurrencies() {
        return currencies;
    }
}
