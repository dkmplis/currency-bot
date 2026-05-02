package bot.currencytrackbot.contexts;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserConversionContextRegistry implements ContextRegistry<UserConversionContext> {
    private final Map<Long, UserConversionContext> contexts = new ConcurrentHashMap<>();

    public void add(long chatId, UserConversionContext context) {
        contexts.put(chatId, context);
    }
    public void remove(long chatId) {
        contexts.remove(chatId);
    }
    public UserConversionContext getContext(long chatId) {
        return contexts.get(chatId);
    }





}
