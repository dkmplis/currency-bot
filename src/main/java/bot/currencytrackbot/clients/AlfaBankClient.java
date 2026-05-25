package bot.currencytrackbot.clients;

import bot.currencytrackbot.dtos.AlfaBankResponseDto;
import bot.currencytrackbot.exceptions.ExternalClientException;
import bot.currencytrackbot.exceptions.ExternalServerException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Component
public class AlfaBankClient {
    private final RestClient client;

    public AlfaBankClient(@Qualifier("alfaBankRestClient")
                             RestClient client) {
        this.client = client;
    }

    @Cacheable("alfaBank")
    @Retryable(includes = {ExternalServerException.class, ResourceAccessException.class},
            excludes = ExternalClientException.class)
    public AlfaBankResponseDto getRate() {
        AlfaBankResponseDto response = client.get()
                .uri("/partner/1.0.0/public/rates")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, ((request, response1) -> {
                    throw new ExternalClientException("Ошибка клиента при обращении к AlfaBankApi");
                }))
                .onStatus(HttpStatusCode::is5xxServerError, (request, response1) -> {
                    throw new ExternalServerException("Ошибка сервера AlfaBankApi");
                })
                .body(AlfaBankResponseDto.class);
        if (response == null ||
                response.rates().isEmpty() ||
                response.rates().size() < 2) {
            throw new ExternalServerException("Api вернуло пустой ответ");
        }

        return response;
    }
}
