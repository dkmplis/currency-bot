package bot.currencytrackbot.telegram.handlers;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Handler {
    BotApiMethod<?> handle(Update update);
}
