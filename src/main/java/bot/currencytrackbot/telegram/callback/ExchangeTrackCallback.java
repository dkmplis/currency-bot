package bot.currencytrackbot.telegram.callback;

import bot.currencytrackbot.contexts.BankSelectedContextRegistry;
import bot.currencytrackbot.utils.BankType;
import bot.currencytrackbot.utils.Capability;
import bot.currencytrackbot.services.ExchangeRateService;
import bot.currencytrackbot.telegram.keyboard.MenuKeyboardGenerator;
import bot.currencytrackbot.utils.BotConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ExchangeTrackCallback implements Callback {
    public static final String DATA = Capability.EXCHANGE_RATE.name();
    private final MenuKeyboardGenerator keyboardGenerator;
    private final Map<BankType, ExchangeRateService> services;
    private final BankSelectedContextRegistry bankSelectedContext;

    public ExchangeTrackCallback(List<ExchangeRateService> services,
                                 MenuKeyboardGenerator keyboardGenerator,
                                 BankSelectedContextRegistry bankSelectedContext) {
           this.bankSelectedContext = bankSelectedContext;
           this.keyboardGenerator = keyboardGenerator;
           this.services = new EnumMap<>(BankType.class);
           services.forEach(s ->
                   this.services.put(s.getBankType(), s)
           );
    }
    @Override
    public BotApiMethod<?> apply(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();
        BankType bankType =
                bankSelectedContext.getContext(chatId);
        ExchangeRateService service = services.get(bankType);
        if (service == null) {
            log.error("Нету сервиса обслуживающего такой банк");
            return EditMessageText.builder()
                    .chatId(chatId)
                    .messageId(messageId)
                    .text(BotConst.EXTERNAL_SERVICE_ERROR)
                    .replyMarkup(keyboardGenerator.generateReturn())
                    .build();
        }
        bankSelectedContext.remove(chatId);
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(service.getExchangeRate())
                .replyMarkup(keyboardGenerator.generateReturn())
                .build();
    }

    @Override
    public boolean supports(String data) {
        return DATA.equals(data);
    }
}
