package bot.currencytrackbot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    private final BotProperties botProperties;
    private final ExternalApiProperties externalApiProperties;

    @Bean
    public TelegramClient telegramClient() {
         return new OkHttpTelegramClient(botProperties.token());
    }

    @Bean
    public RestClient belarusBankRestClient() {

        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(
                externalApiProperties.belarusBankApi().connectTimeout()
        );
        factory.setReadTimeout(
                externalApiProperties.belarusBankApi().readTimeout()
        );

        return RestClient.builder()
                .baseUrl(externalApiProperties.belarusBankApi().baseUrl())
                .defaultHeader("Accept", "application/json")
                .requestFactory(factory)
                .build();
    }

    @Bean
    public RestClient alfaBankRestClient() {
        SimpleClientHttpRequestFactory factory =
                new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(
                externalApiProperties.alfaBankApi().connectTimeout()
        );
        factory.setReadTimeout(
                externalApiProperties.alfaBankApi().readTimeout()
        );

        return RestClient.builder()
                .baseUrl(externalApiProperties.alfaBankApi().baseUrl())
                .defaultHeader("Accept", "application/json")
                .requestFactory(factory)
                .build();
    }


}
