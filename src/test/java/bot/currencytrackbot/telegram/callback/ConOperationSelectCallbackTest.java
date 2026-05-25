package bot.currencytrackbot.telegram.callback;

import bot.currencytrackbot.contexts.ContextRegistry;
import bot.currencytrackbot.contexts.UserConversionContext;
import bot.currencytrackbot.telegram.keyboard.MenuKeyboardGenerator;
import bot.currencytrackbot.utils.BankType;
import bot.currencytrackbot.utils.BotConst;
import bot.currencytrackbot.utils.ConversionOperations;
import bot.currencytrackbot.utils.MessageFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConOperationSelectCallbackTest {

    @Mock
    private ContextRegistry<UserConversionContext> conversionContext;

    @Mock
    private ContextRegistry<BankType> bankContext;

    @Mock
    private MenuKeyboardGenerator keyboardGenerator;

    @Mock
    private MessageFactory messageFactory;

    private ConOperationSelectCallback callback;

    @BeforeEach
    void setUp() {
        callback = new ConOperationSelectCallback(
                conversionContext,
                bankContext,
                keyboardGenerator,
                messageFactory
        );
    }

    @Test
    void apply_SuccessPath_ReturnsEditMessageText() {
        long chatId = 123L;
        int messageId = 456;
        String operationStr = ConversionOperations.BUY.name();
        BankType expectedBank = BankType.ALFABANK;

        Update mockUpdate = createMockUpdate(chatId, messageId, operationStr);
        when(bankContext.getContext(chatId)).thenReturn(expectedBank);

        BotApiMethod<?> result = callback.apply(mockUpdate);

        assertInstanceOf(EditMessageText.class, result);
        EditMessageText editMessageText = (EditMessageText) result;

        assertEquals(String.valueOf(chatId), editMessageText.getChatId());
        assertEquals(messageId, editMessageText.getMessageId());
        assertEquals(BotConst.SELECT_CURRENCY_FROM, editMessageText.getText());

        ArgumentCaptor<UserConversionContext> contextCaptor = ArgumentCaptor.forClass(UserConversionContext.class);
        verify(conversionContext, times(1)).add(eq(chatId), contextCaptor.capture());

        assertEquals(ConversionOperations.BUY, contextCaptor.getValue().getOperationType());
    }

    @Test
    void apply_BankContextNull_ReturnsExpiredSessionMessage() {
        long chatId = 123L;
        Update mockUpdate = createMockUpdate(chatId, 456, ConversionOperations.SELL.name());

        when(bankContext.getContext(chatId)).thenReturn(null);

        SendMessage expectedErrorMessage = new SendMessage(String.valueOf(chatId), BotConst.EXPIRED_SESSION_ERROR);
        when(messageFactory.expired_session_bank_selected_message(chatId)).thenReturn(expectedErrorMessage);

        BotApiMethod<?> result = callback.apply(mockUpdate);

        assertEquals(expectedErrorMessage, result);

        verifyNoInteractions(conversionContext);
        verifyNoInteractions(keyboardGenerator);
    }


    @ParameterizedTest
    @ValueSource(strings = {"BUY", "SELL"})
    void supports_ValidData_ReturnsTrue(String data) {
        assertTrue(callback.supports(data));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"INVALID", "EXCHANGE_RATE", "buy"})
    void supports_InvalidData_ReturnsFalse(String data) {
        assertFalse(callback.supports(data));
    }

    private Update createMockUpdate(long chatId, int messageId, String callbackData) {
        Update update = mock(Update.class);
        CallbackQuery callbackQuery = mock(CallbackQuery.class);
        Message message = mock(Message.class);

        when(update.getCallbackQuery()).thenReturn(callbackQuery);
        when(callbackQuery.getMessage()).thenReturn(message);

        when(callbackQuery.getData()).thenReturn(callbackData);
        when(message.getChatId()).thenReturn(chatId);

        lenient().when(message.getMessageId()).thenReturn(messageId);

        return update;
    }
}
