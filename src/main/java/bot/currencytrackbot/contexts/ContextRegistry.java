package bot.currencytrackbot.contexts;

public interface ContextRegistry<T> {
    void remove(long chatId);
    T getContext(long chatId);
    void add(long chatId,T context);
}
