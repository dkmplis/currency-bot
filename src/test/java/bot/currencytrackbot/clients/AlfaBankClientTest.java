package bot.currencytrackbot.clients;

import bot.currencytrackbot.exceptions.ExternalServerException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AlfaBankClientTest {

    private static WireMockServer wireMockServer;
    private AlfaBankClient alfaBankClient;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(0);
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @BeforeEach
    void setUp() {
        WireMock.reset();
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:" + wireMockServer.port())
                .build();

        alfaBankClient = new AlfaBankClient(restClient);

    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @Test
    void getRate_ServerError_ThrowsException() {
        stubFor(get(urlEqualTo("/partner/1.0.0/public/rates"))
                .willReturn(aResponse().withStatus(500)));

        assertThrows(ExternalServerException.class, () -> {
            alfaBankClient.getRate();
        });
    }

    @Test
    void getRate_EmptyResponse_ThrowsException() {
        String emptyJson = "{\"rates\": []}";

        stubFor(get(urlEqualTo("/partner/1.0.0/public/rates"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(emptyJson)));

        assertThrows(ExternalServerException.class, () -> {
            alfaBankClient.getRate();
        });
    }

}
