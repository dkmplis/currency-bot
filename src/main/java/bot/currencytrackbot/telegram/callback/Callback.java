package bot.currencytrackbot.telegram.callback;

import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Callback {
    BotApiMethod<?> apply(Update update);
    boolean supports(String data);
}
