package bot.currencytrackbot.telegram.callback;

import bot.currencytrackbot.utils.BankType;
import bot.currencytrackbot.utils.Currency;
import bot.currencytrackbot.contexts.ContextRegistry;
import bot.currencytrackbot.contexts.UserConversionContext;
import bot.currencytrackbot.services.ConvertService;
import bot.currencytrackbot.telegram.keyboard.MenuKeyboardGenerator;
import bot.currencytrackbot.utils.BotConst;
import bot.currencytrackbot.utils.MessageFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CurrencyConvertCallback implements Callback {
    private static final Set<String> DATA = Arrays.stream(Currency.values())
            .map(Enum::name)
            .collect(Collectors.toSet());
    private final ContextRegistry<UserConversionContext> conversionContext;
    private final ContextRegistry<BankType> bankContext;
    private final MenuKeyboardGenerator keyboardGenerator;
    private final Map<BankType, ConvertService> services;
    private final MessageFactory messageFactory;

    public CurrencyConvertCallback(
            @Qualifier("userConversionContextRegistry")
            ContextRegistry<UserConversionContext> conversionContext,
            @Qualifier("bankSelectedContextRegistry") ContextRegistry<BankType> bankContext,
            MenuKeyboardGenerator keyboardGenerator,
            List<ConvertService> services,
            MessageFactory messageFactory) {
        this.conversionContext = conversionContext;
        this.bankContext = bankContext;
        this.keyboardGenerator = keyboardGenerator;
        this.messageFactory = messageFactory;
        this.services = new EnumMap<>(BankType.class);
        services.forEach(s ->
                this.services.put(s.getBankType(), s)
        );
    }

    @Override
    public BotApiMethod<?> apply(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        String data = update.getCallbackQuery().getData();
        Currency currency = Currency.valueOf(data);
        BankType bankType = bankContext.getContext(chatId);
        if (bankType == null) {
            return messageFactory.expired_session_bank_selected_message(chatId);
        }
        UserConversionContext userContext = conversionContext.getContext(chatId);
        if (userContext == null) {
            return messageFactory.expired_session_operation_selected_massage(chatId, bankType);
        }
        if (userContext.getCurrencyFrom() == null) {
            userContext.setCurrencyFrom(currency);
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .text(BotConst.ENTER_AMOUNT)
                    .build();
        } else {
            ConvertService service = services.get(bankType);
            if (service == null) {
                log.error("Нету сервиса обслуживающего такой банк");
                throw new IllegalStateException("Нету сервиса обслуживающего такой банк");
            }
            conversionContext.remove(chatId);
            String result = service.convert(userContext.getCurrencyFrom(),
                    currency, userContext.getSum(),
                    userContext.getOperationType());
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(update.getCallbackQuery().getMessage().getMessageId())
                    .text(result)
                    .replyMarkup(keyboardGenerator.generateMenuOperations(bankType))
                    .build();
        }


    }

    @Override
    public boolean supports(String data) {
        return DATA.contains(data);
    }
}
