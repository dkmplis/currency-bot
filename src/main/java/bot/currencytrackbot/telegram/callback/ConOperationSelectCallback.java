package bot.currencytrackbot.telegram.callback;

import bot.currencytrackbot.utils.BankType;
import bot.currencytrackbot.utils.ConversionOperations;
import bot.currencytrackbot.contexts.ContextRegistry;
import bot.currencytrackbot.contexts.UserConversionContext;
import bot.currencytrackbot.telegram.keyboard.MenuKeyboardGenerator;
import bot.currencytrackbot.utils.BotConst;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ConOperationSelectCallback implements Callback{
    private static final Set<String> DATA =
            Arrays.stream(ConversionOperations.values())
            .map(Enum::name)
            .collect(Collectors.toSet());
    private final ContextRegistry<UserConversionContext> conversionContext;
    private final ContextRegistry<BankType> bankContext;
    private final MenuKeyboardGenerator keyboardGenerator;

    public ConOperationSelectCallback(
            @Qualifier("userConversionContextRegistry")
            ContextRegistry<UserConversionContext> conversionContext,
            @Qualifier("bankSelectedContextRegistry") ContextRegistry<BankType> bankContext,
            MenuKeyboardGenerator keyboardGenerator) {
        this.conversionContext = conversionContext;
        this.bankContext = bankContext;
        this.keyboardGenerator = keyboardGenerator;
    }
    @Override
    public BotApiMethod<?> apply(Update update) {
        String message = update.getCallbackQuery().getData();
        ConversionOperations operations = ConversionOperations.valueOf(message);
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        BankType bankType = bankContext.getContext(chatId);
        UserConversionContext userConversionDto = new UserConversionContext(operations);
        conversionContext.add(chatId, userConversionDto);
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .text(BotConst.SELECT_CURRENCY_FROM)
                .replyMarkup(keyboardGenerator.generateMenuCurrency(bankType))
                .build();
    }

    @Override
    public boolean supports(String data) {
        return DATA.contains(data);
    }
}
