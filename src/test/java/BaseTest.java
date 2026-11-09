import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Random;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static io.restassured.RestAssured.given;

public class BaseTest {
    static final String APP_URL = "http://localhost:8080";
    static final String API_KEY = "qazWSXedc";
    @RegisterExtension
    static WireMockExtension mock = WireMockExtension.newInstance()
            .options(options().port(8888))
            .build();
    @Step("Отправить запрос: токен «{token}», действие «{action}»")
    Response send (String token, String action){
    return given()
            .baseUri(APP_URL)
            .header("X-Api-Key", API_KEY)
            .contentType("application/x-www-form-urlencoded")
            .formParam("token", token)
            .formParam("action", action)
            .when()
            .post("/endpoint");
    }
    @Step("Внешний сервис на /auth отвечает кодом {status}")
    void authReturn(int status){
        mock.stubFor(
                post(urlPathMatching("/+auth"))
                        .willReturn(aResponse().withStatus(status))
        );
    }
    @Step("Внешний сервис на /doAction отвечает кодом {status}")
    void doActionReturn(int status){
        mock.stubFor(
                post(urlPathMatching("/+doAction"))
                        .willReturn(aResponse().withStatus(status))
        );
    }
    @Step("Генератор рандомного Токена")
    String randToken(){
        String chars = "ABCDEF0123456789";
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i =0; i<32; i++){
            int index = rand.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
}
