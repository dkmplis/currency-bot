package bot.currencytrackbot.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AmountParserTest {

    @ParameterizedTest
    @ValueSource(strings = {"100", "100.5", "100.50", "100,5", " 100 ", "10 000"})
    void shouldParserValidStrings(String input) {
        String expectedString = input.trim()
                .replace(" ", "")
                .replace(",", ".");
        BigDecimal expected = new BigDecimal(expectedString);

        BigDecimal actual = AmountParser.parseAmount(input);

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"-5", "0", "abc", "", "100.5.5", "   "})
    void shouldThrowExceptionOnInvalidInput(String input) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> AmountParser.parseAmount(input)
        );
        assertNotNull(exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"100", "100.5", "100,50"})
    void isValid_ValidStrings_ReturnsTrue(String input) {
        assertTrue(AmountParser.isValid(input));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"abc", "-10", "100.555"})
    void isValid_InvalidStrings_ReturnsFalse(String input) {
        assertFalse(AmountParser.isValid(input));
    }
}
