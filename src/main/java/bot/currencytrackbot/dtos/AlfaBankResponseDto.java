package bot.currencytrackbot.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record AlfaBankResponseDto(
        @JsonIgnoreProperties(ignoreUnknown = true)
        List<AlfaRates> rates
) {
    public record AlfaRates(
            String sellRate,
            String sellIso,
            String buyRate,
            String buyIso
    ) {

    }
}
