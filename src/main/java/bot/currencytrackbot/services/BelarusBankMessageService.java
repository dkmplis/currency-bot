package bot.currencytrackbot.services;

import bot.currencytrackbot.utils.BankType;
import bot.currencytrackbot.clients.BelarusBankClient;
import bot.currencytrackbot.dtos.BelarusBankResponseDto;
import bot.currencytrackbot.exceptions.ExternalClientException;
import bot.currencytrackbot.exceptions.ExternalServerException;
import bot.currencytrackbot.utils.BotConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BelarusBankMessageService implements ExchangeRateService {

    private String lastMessage = BotConst.EXTERNAL_SERVICE_ERROR;
    private final BelarusBankClient belarusBankClient;

    @Override
    public String getExchangeRate() {
        try {
            List<BelarusBankResponseDto> response =
                    belarusBankClient.getRate();
            BelarusBankResponseDto firstDto = response.get(0);

            lastMessage = BotConst.EXCHANGE_RATE_RESPONSE.formatted(
                    normalizeRate(firstDto.usdIn()),
                    normalizeRate(firstDto.usdOut()),
                    normalizeRate(firstDto.eurIn()),
                    normalizeRate(firstDto.eurOut()),
                    normalizeRate(firstDto.rubIn()),
                    normalizeRate(firstDto.rubOut())
            );
            return lastMessage;
        } catch (ExternalClientException | ExternalServerException e) {
            log.error(e.getMessage(), e);
            return lastMessage;
        }
    }

    @Override
    public BankType getBankType() {
        return BankType.BELARUSBANK;
    }

    private String normalizeRate(String value) {
        if (value == null || value.isBlank() || "0.0000".equals(value.trim())) {
            return "Нет данных";
        }
        return value.trim();
    }
}
