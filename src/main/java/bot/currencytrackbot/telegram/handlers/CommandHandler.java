package bot.currencytrackbot.telegram.handlers;

import bot.currencytrackbot.telegram.command.Command;
import bot.currencytrackbot.utils.BotConst;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CommandHandler implements Handler{

    private final Map<String, Command> commands;

    public CommandHandler(List<Command> commands) {
        this.commands = commands.stream().collect(Collectors.toMap(
                        Command::getCommand, Function.identity()));
    }

    public SendMessage handle(Update update) {
        String command = update.getMessage().getText().split(" ")[0];
        Command handler = commands.get(command);
        if (handler == null) {
            return SendMessage.builder()
                    .text(BotConst.UNKNOWN_COMMAND)
                    .chatId(update.getMessage().getChatId())
                    .build();
        }
        return handler.apply(update);
    }
}
