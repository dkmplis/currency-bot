package bot.currencytrackbot.services;

import bot.currencytrackbot.utils.ConversionOperations;
import bot.currencytrackbot.utils.Currency;

import java.math.BigDecimal;

public interface ConvertService extends MessageService{
    String convert(Currency from, Currency to, BigDecimal amount,
                   ConversionOperations operations);
}
