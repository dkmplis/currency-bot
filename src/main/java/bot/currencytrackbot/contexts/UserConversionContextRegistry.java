package bot.currencytrackbot.contexts;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class UserConversionContextRegistry implements ContextRegistry<UserConversionContext> {
    private final Cache<Long, UserConversionContext> contexts =
            Caffeine.newBuilder()
                    .expireAfterAccess(Duration.ofMinutes(15))
                    .maximumSize(5000)
                    .build();

    public void add(long chatId, UserConversionContext context) {
        contexts.put(chatId, context);
    }

    public void remove(long chatId) {
        contexts.invalidate(chatId);
    }

    public UserConversionContext getContext(long chatId) {
        return contexts.getIfPresent(chatId);
    }


}
