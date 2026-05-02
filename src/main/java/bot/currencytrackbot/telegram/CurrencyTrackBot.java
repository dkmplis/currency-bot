package bot.currencytrackbot.telegram;

import bot.currencytrackbot.config.BotProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

@Component
@RequiredArgsConstructor
public class CurrencyTrackBot implements SpringLongPollingBot {

    private final CurrencyTranslatorConsumer translatorConsumer;
    private final BotProperties botProperties;



    @Override
    public String getBotToken() {
        return botProperties.token();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return translatorConsumer;
    }
}
