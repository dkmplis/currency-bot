package bot.currencytrackbot.telegram.freeText;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface FreeText {
    BotApiMethod<?> apply(Update update);
    boolean supports(String message);
}
