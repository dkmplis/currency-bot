package bot.currencytrackbot.telegram.command;

import bot.currencytrackbot.telegram.keyboard.MenuKeyboardGenerator;
import bot.currencytrackbot.utils.BotConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command{
    public static final String COMMAND_START = "/start";

    private final MenuKeyboardGenerator keyboardGenerator;
    @Override
    public SendMessage apply(Update update) {
        return SendMessage.builder()
                .text(BotConst.RESPONSE_TO_START_BANK_SELECTION)
                .chatId(update.getMessage().getChatId())
                .replyMarkup(keyboardGenerator.generateMenuBank())
                .build();
    }

    @Override
    public String getCommand() {
        return COMMAND_START;
    }

}
