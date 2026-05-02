package bot.currencytrackbot.services;

import bot.currencytrackbot.utils.BankType;
import bot.currencytrackbot.utils.ConversionOperations;
import bot.currencytrackbot.utils.Currency;
import bot.currencytrackbot.clients.AlfaBankClient;
import bot.currencytrackbot.dtos.AlfaBankResponseDto;
import bot.currencytrackbot.exceptions.ExternalClientException;
import bot.currencytrackbot.exceptions.ExternalServerException;
import bot.currencytrackbot.utils.BotConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlfaBankMessageService implements ExchangeRateService, ConvertService {
    private String lastMessage = BotConst.EXTERNAL_SERVICE_ERROR;
    private final AlfaBankClient client;

    @Override
    public String getExchangeRate() {
        try {
            AlfaBankResponseDto responseDto = client.getRate();
            Map<String, AlfaBankResponseDto.AlfaRates> byCurrency =
                    responseDto.rates().stream()
                    .filter(r -> Currency.BYN.name().equals(r.buyIso()))
                    .filter(r -> Currency.USD.name().equals(r.sellIso())||
                            Currency.EUR.name().equals(r.sellIso())||
                            Currency.RUB.name().equals(r.sellIso()))
                            .collect(Collectors.toMap(
                                    AlfaBankResponseDto.AlfaRates::sellIso,
                                    Function.identity(),
                                    (left, right) -> left
                            ));
            lastMessage = format(byCurrency);
            return lastMessage;
        } catch (ExternalClientException | ExternalServerException e) {
            log.error(e.getMessage(), e);
            return lastMessage;
        }
    }

    @Override
    public String convert(Currency from, Currency to,
                          BigDecimal amount, ConversionOperations operations) {
        try {
            AlfaBankResponseDto rates = client.getRate();
            AlfaBankResponseDto.AlfaRates rate =
                    rates.rates().stream()
                            .filter(r ->
                                    from.name().equals(r.sellIso()) &&
                                            to.name().equals(r.buyIso()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException(
                                    "Rate not found for from=" + from + ", to=" + to
                            ));
            return calculate(rate, amount, operations).toString();
        } catch (ExternalClientException | ExternalServerException e) {
            log.error(e.getMessage(), e);
            return lastMessage;
        }
    }

    private BigDecimal calculate(AlfaBankResponseDto.AlfaRates rate,
                                 BigDecimal amount, ConversionOperations operations) {
        BigDecimal selectedRate =
                switch (operations) {
                    case BUY -> new BigDecimal(rate.sellRate());
                    case SELL -> new BigDecimal(rate.buyRate());
                };

        return amount.multiply(selectedRate)
                .setScale(2, RoundingMode.HALF_UP);

    }

    private String format(Map<String, AlfaBankResponseDto.AlfaRates> byCurrency) {
        return BotConst.EXCHANGE_RATE_RESPONSE.formatted(
                normalize(byCurrency.get(Currency.USD.name()).sellRate()),
                normalize(byCurrency.get(Currency.USD.name()).buyRate()),
                normalize(byCurrency.get(Currency.EUR.name()).sellRate()),
                normalize(byCurrency.get(Currency.EUR.name()).buyRate()),
                normalize(byCurrency.get(Currency.RUB.name()).sellRate()),
                normalize(byCurrency.get(Currency.RUB.name()).buyRate())
        );
    }

    private String normalize(String value) {
        if (value == null || value.isBlank() || "0".equals(value)) {
            return "Нет данных";
        }
        return value.trim();
    }

    @Override
    public BankType getBankType() {
        return BankType.ALFABANK;
    }
}
