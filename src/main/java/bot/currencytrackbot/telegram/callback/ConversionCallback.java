package bot.currencytrackbot.telegram.callback;

import bot.currencytrackbot.utils.Capability;
import bot.currencytrackbot.telegram.keyboard.MenuKeyboardGenerator;
import bot.currencytrackbot.utils.BotConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
@Component
@RequiredArgsConstructor
public class ConversionCallback implements Callback{
    public static final String DATA = Capability.CONVERT_VALUE.name();
    private final MenuKeyboardGenerator keyboardGenerator;
    @Override
    public BotApiMethod<?> apply(Update update) {

        return EditMessageText.builder()
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .messageId(update.getCallbackQuery().getMessage().getMessageId())
                .text(BotConst.SELECT_OPERATION)
                .replyMarkup(keyboardGenerator.generateMenuBuyOrSell())
                .build();
    }

    @Override
    public boolean supports(String data) {
        return data.equals(DATA);
    }
}
