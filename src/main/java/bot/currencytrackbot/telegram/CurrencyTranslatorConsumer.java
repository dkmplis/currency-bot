package bot.currencytrackbot.telegram;

import bot.currencytrackbot.telegram.handlers.CallbackHandler;
import bot.currencytrackbot.telegram.handlers.CommandHandler;
import bot.currencytrackbot.telegram.handlers.FreeTextHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyTranslatorConsumer
        implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final CommandHandler commandHandler;
    private final CallbackHandler callbackHandler;
    private final FreeTextHandler freeTextHandler;

    @Override
    public void consume(Update update) {
        if (update.hasCallbackQuery()) {
            sendMessage(callbackHandler.handle(update));
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            if (message.startsWith("/")) {
                sendMessage(commandHandler.handle(update));
            } else {
                sendMessage(freeTextHandler.handle(update));
            }
        }
    }

    private void sendMessage(BotApiMethod<?> message) {
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка Telegram API: {}",e.getMessage(), e);
        }
    }
}
