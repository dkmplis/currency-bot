package bot.currencytrackbot.telegram.callback;

import bot.currencytrackbot.contexts.BankSelectedContextRegistry;
import bot.currencytrackbot.utils.BankType;
import bot.currencytrackbot.telegram.keyboard.MenuKeyboardGenerator;
import bot.currencytrackbot.utils.BotConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class BankSelectCallback implements Callback{
    public static final String DATA = "BANK_SELECT:";
    private final BankSelectedContextRegistry bankSelectedContext;
    private final MenuKeyboardGenerator keyboardGenerator;

    @Override
    public BotApiMethod<?> apply(Update update) {
        String data = update.getCallbackQuery().getData();
        BankType bankType = BankType.valueOf(data.substring(DATA.length()));
        bankSelectedContext.add(update.getCallbackQuery().getMessage().getChatId(), bankType);
        return EditMessageText.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .text(BotConst.SELECT_OPERATION)
                .replyMarkup(keyboardGenerator.generateMenuOperations(bankType))
                .build();
    }

    @Override
    public boolean supports(String data) {
        if (data == null) {
            return false;
        }
        return data.startsWith(DATA);
    }
}
