package bot.currencytrackbot.utils;

import bot.currencytrackbot.contexts.BankSelectedContextRegistry;
import bot.currencytrackbot.telegram.keyboard.MenuKeyboardGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@RequiredArgsConstructor
public class MessageFactory {

    private final MenuKeyboardGenerator keyboardGenerator;
    private final BankSelectedContextRegistry bankSelectedContextRegistry;

    public SendMessage expired_session_bank_selected_message(long chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(BotConst.EXPIRED_SESSION_ERROR)
                .replyMarkup(keyboardGenerator.generateMenuBank())
                .build();
    }

    public SendMessage expired_session_operation_selected_massage(long chatId,
                                                                  BankType type) {
        return SendMessage.builder()
                .text(BotConst.EXPIRED_SESSION_ERROR)
                .replyMarkup(keyboardGenerator.generateMenuOperations(type))
                .build();
    }
}
