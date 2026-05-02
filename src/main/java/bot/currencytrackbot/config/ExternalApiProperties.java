package bot.currencytrackbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
@ConfigurationProperties(prefix = "external-api")
public record ExternalApiProperties(
        BankApiProperties belarusBankApi,
        BankApiProperties alfaBankApi
) {
    public record BankApiProperties(
            String baseUrl,
            Duration connectTimeout,
            Duration readTimeout
    ){}
}
