package bot.currencytrackbot.telegram.handlers;

import bot.currencytrackbot.telegram.freeText.FreeText;
import bot.currencytrackbot.utils.BotConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FreeTextHandler implements Handler{
    private final List<FreeText> freeTextList;

    @Override
    public BotApiMethod<?> handle(Update update) {
        String message = update.getMessage().getText();
        for (FreeText freeText : freeTextList) {
            if (freeText.supports(message)) return freeText.apply(update);
        }
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId())
                .text(BotConst.UNKNOWN_COMMAND)
                .build();
    }


}
