package bot.currencytrackbot.contexts;

import bot.currencytrackbot.utils.BankType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class BankSelectedContextRegistry implements ContextRegistry<BankType>{
    private final Map<Long, BankType> context = new ConcurrentHashMap<>() {};

    public void add(long chatId, BankType type) {
        context.put(chatId, type);
    }

    public void remove(long chatId) {
        context.remove(chatId);
    }

    public BankType getContext(long chatId) {
        return context.get(chatId);
    }
}
