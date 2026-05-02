package bot.currencytrackbot.telegram.freeText;

import bot.currencytrackbot.utils.BankType;
import bot.currencytrackbot.contexts.ContextRegistry;
import bot.currencytrackbot.contexts.UserConversionContext;
import bot.currencytrackbot.telegram.keyboard.MenuKeyboardGenerator;
import bot.currencytrackbot.utils.AmountParser;
import bot.currencytrackbot.utils.BotConst;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.math.BigDecimal;
@Component
public class ConValueText implements FreeText{
    private final MenuKeyboardGenerator keyboardGenerator;
    private final ContextRegistry<UserConversionContext> contextRegistry;
    private final ContextRegistry<BankType> bankTypeContextRegistry;

    public ConValueText(@Qualifier("userConversionContextRegistry")
                        ContextRegistry<UserConversionContext> contextRegistry,
                        @Qualifier("bankSelectedContextRegistry")
                        ContextRegistry<BankType> bankTypeContextRegistry,
                        MenuKeyboardGenerator keyboardGenerator) {
        this.contextRegistry = contextRegistry;
        this.keyboardGenerator = keyboardGenerator;
        this.bankTypeContextRegistry = bankTypeContextRegistry;
    }

    @Override
    public BotApiMethod<?> apply(Update update) {
        try {
            long chatId = update.getMessage().getChatId();
            BigDecimal amount =
                    AmountParser.parseAmount(update.getMessage().getText());
            UserConversionContext context = contextRegistry.getContext(chatId);
            BankType type = bankTypeContextRegistry.getContext(chatId);
            if (type == null) {
                return SendMessage.builder()
                        .chatId(chatId)
                        .text(BotConst.SELECT_BANK)
                        .replyMarkup(keyboardGenerator.generateMenuBank())
                        .build();
            }
            if (context == null) {
                return SendMessage.builder()
                        .chatId(chatId)
                        .text(BotConst.SELECT_OPERATION)
                        .replyMarkup(keyboardGenerator.generateMenuOperations(type))
                        .build();
            }
            context.setSum(amount);
            return SendMessage.builder()
                    .chatId(chatId)
                    .text(BotConst.SELECT_CURRENCY_TO)
                    .replyMarkup(keyboardGenerator.generateMenuCurrency(type))
                    .build();

        } catch (IllegalArgumentException e) {
            return SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(BotConst.INVALID_AMOUNT + e.getMessage())
                    .build();
        }
    }

    @Override
    public boolean supports(String message) {
        return AmountParser.isValid(message);
    }


}
