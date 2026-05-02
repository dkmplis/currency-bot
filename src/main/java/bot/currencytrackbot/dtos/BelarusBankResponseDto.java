package bot.currencytrackbot.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BelarusBankResponseDto(
        @JsonProperty("USD_in") String usdIn,
        @JsonProperty("USD_out") String usdOut,
        @JsonProperty("EUR_in") String eurIn,
        @JsonProperty("EUR_out") String eurOut,
        @JsonProperty("RUB_in") String rubIn,
        @JsonProperty("RUB_out") String rubOut,
        @JsonProperty("CNY_in") String cnyIn,
        @JsonProperty("CNY_out") String cnyOut,
        @JsonProperty("USD_EUR_in") String usdEurIn,
        @JsonProperty("USD_EUR_out") String usdEurOut,
        @JsonProperty("USD_RUB_in") String usdRubIn,
        @JsonProperty("USD_RUB_out") String usdRubOut,
        @JsonProperty("RUB_EUR_in") String rubEurIn,
        @JsonProperty("RUB_EUR_out") String rubEurOut,
        @JsonProperty("CNY_USD_in") String cnyUsdIn,
        @JsonProperty("CNY_USD_out") String cnyUsdOut
) {

}
