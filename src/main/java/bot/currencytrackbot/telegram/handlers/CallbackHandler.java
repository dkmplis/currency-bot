package bot.currencytrackbot.telegram.handlers;

import bot.currencytrackbot.telegram.callback.Callback;
import bot.currencytrackbot.utils.BotConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CallbackHandler implements Handler{
    private final List<Callback> callbacks;


    public BotApiMethod<?> handle(Update update) {
        String data = update.getCallbackQuery().getData();

        for (Callback callback : callbacks) {
            if (callback.supports(data)) return callback.apply(update);
        }
        log.error("Неизвестный callback");
        return SendMessage.builder()
                .text(BotConst.UNKNOWN_COMMAND)
                .chatId(update.getCallbackQuery().getMessage().getChatId())
                .build();

    }
}
