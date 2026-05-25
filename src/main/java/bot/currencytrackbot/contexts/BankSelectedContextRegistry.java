package bot.currencytrackbot.contexts;

import bot.currencytrackbot.utils.BankType;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class BankSelectedContextRegistry implements ContextRegistry<BankType>{

    private final Cache<Long, BankType> context = Caffeine.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(15))
            .maximumSize(5000)
            .build();

    public void add(long chatId, BankType type) {
        context.put(chatId, type);
    }

    public void remove(long chatId) {
        context.invalidate(chatId);
    }

    public BankType getContext(long chatId) {
        return context.getIfPresent(chatId);
    }
}
