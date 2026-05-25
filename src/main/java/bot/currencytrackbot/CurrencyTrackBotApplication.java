package bot.currencytrackbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableResilientMethods
@EnableCaching
public class CurrencyTrackBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyTrackBotApplication.class, args);
    }

}
