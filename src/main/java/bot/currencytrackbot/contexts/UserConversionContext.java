package bot.currencytrackbot.contexts;

import bot.currencytrackbot.utils.ConversionOperations;
import bot.currencytrackbot.utils.Currency;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class UserConversionContext {
    private ConversionOperations operationType;
    private BigDecimal sum;
    private Currency currencyFrom;

    public UserConversionContext(ConversionOperations operationType) {
        this.operationType = operationType;
    }
}
