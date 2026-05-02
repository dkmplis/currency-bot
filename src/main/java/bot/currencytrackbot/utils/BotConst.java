package bot.currencytrackbot.utils;

public class BotConst {

    public static final String RESPONSE_TO_START_BANK_SELECTION =
            "Добро пожаловать, выберите банк который вас интересует";
    public static final String UNKNOWN_COMMAND =
            "Неизвестная команда";
    public static final String EXCHANGE_RATE_RESPONSE =
            """
                Курсы валют

                USD: покупка %s | продажа %s
                EUR: покупка %s | продажа %s
                RUB: покупка %s | продажа %s
                """;

    public static final String BUTTON_NAME_EXCHANGE_RATE = "Курс валют";
    public static final String BUTTON_NAME_RETURN_MENU_BANK_SELECTIONS =
            "Выбор банка";

    public static final String EXTERNAL_SERVICE_ERROR =
            "Ошибка сервиса. Повторите попозже";

    public static final String BUTTON_NAME_CONVERT_VALUE = "Конвертация валют";
    public static final String SELECT_OPERATION = "Выберите операцию";
    public static final String SELECT_BANK = "Выберите банк";
    public static final String SELECT_CURRENCY_TO =
            "Выберите валюту в которую нужно конвертировать";
    public static final String SELECT_CURRENCY_FROM =
            "Выберите валюту которую нужно конвертировать";
    public static final String ENTER_AMOUNT = "Введите сумму";
    public static final String INVALID_AMOUNT =
            "Некорректная сумма, введите другую:";
}

