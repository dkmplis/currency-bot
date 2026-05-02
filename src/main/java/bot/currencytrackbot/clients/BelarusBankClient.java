package bot.currencytrackbot.clients;

import bot.currencytrackbot.dtos.BelarusBankResponseDto;
import bot.currencytrackbot.exceptions.ExternalClientException;
import bot.currencytrackbot.exceptions.ExternalServerException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class BelarusBankClient {

    private static final ParameterizedTypeReference<List<BelarusBankResponseDto>> RESPONSE_TYPE =
            new ParameterizedTypeReference<List<BelarusBankResponseDto>>() {
            };

    private final RestClient client;

    public BelarusBankClient(@Qualifier("belarusBankRestClient")
                             RestClient client) {
        this.client = client;
    }

    @Retryable(includes = {ExternalServerException.class, ResourceAccessException.class},
            excludes = ExternalClientException.class)
    public List<BelarusBankResponseDto> getRate() {
        List<BelarusBankResponseDto> response = client.get()
                .uri("/kursExchange")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response1) -> {
                    throw new ExternalClientException("Ошибка клиента при обращении к BelarusBankApi");
                }))
                .onStatus(HttpStatusCode::is5xxServerError, (request, response1) -> {
                    throw new ExternalServerException("Ошибка сервера BelarusBankApi");
                })
                .body(RESPONSE_TYPE);
        if (response == null || response.isEmpty() || response.size() < 2) {
            throw new ExternalServerException("Api вернуло пустой ответ");
        }

        return response;
    }


}
