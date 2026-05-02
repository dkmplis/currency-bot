package bot.currencytrackbot.telegram.keyboard;

import bot.currencytrackbot.utils.BankType;
import bot.currencytrackbot.utils.Capability;
import bot.currencytrackbot.utils.ConversionOperations;
import bot.currencytrackbot.utils.Currency;
import bot.currencytrackbot.telegram.callback.BankSelectCallback;
import bot.currencytrackbot.telegram.callback.ReturnMenuBankSelectionCallback;
import bot.currencytrackbot.utils.BotConst;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class MenuKeyboardGenerator {

    public InlineKeyboardMarkup generateMenuBank() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        for (BankType bankType : BankType.values()) {
            rows.add(new InlineKeyboardRow(
                    InlineKeyboardButton.builder()
                            .text(bankType.getTitle())
                            .callbackData(BankSelectCallback.DATA + bankType.name())
                            .build()
            ));
        }
        return new InlineKeyboardMarkup(rows);

    }

    public InlineKeyboardMarkup generateMenuCurrency(BankType type) {
        Set<Currency> currencies = type.getCurrencies();
        List<InlineKeyboardRow> rows = new ArrayList<>();
        for (Currency currency : currencies) {
            rows.add(new InlineKeyboardRow(
                    InlineKeyboardButton.builder()
                            .text(currency.name())
                            .callbackData(currency.name())
                            .build()
            ));
        }
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup generateMenuBuyOrSell() {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        for (ConversionOperations conversionOperation : ConversionOperations.values()) {
            rows.add(new InlineKeyboardRow(
                    InlineKeyboardButton.builder()
                            .text(conversionOperation.getOperation())
                            .callbackData(conversionOperation.name())
                            .build()
            ));
        }
        return new InlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup generateReturn() {
        return new InlineKeyboardMarkup(
                List.of(returnRow())
        );
    }

    public InlineKeyboardMarkup generateMenuOperations(BankType bankType) {
        List<InlineKeyboardRow> rows = new ArrayList<>();
        for (Capability capability : bankType.getCapabilities()) {
            rows.add(new InlineKeyboardRow(
                    InlineKeyboardButton.builder()
                            .text(capability.getOperation())
                            .callbackData(capability.name())
                            .build()
            ));
        }
        rows.add(returnRow());
        return new InlineKeyboardMarkup(rows);
    }

    private InlineKeyboardRow returnRow() {
        return new InlineKeyboardRow(
                InlineKeyboardButton.builder()
                        .text(BotConst.BUTTON_NAME_RETURN_MENU_BANK_SELECTIONS)
                        .callbackData(ReturnMenuBankSelectionCallback.DATA)
                        .build()
        );
    }
}
