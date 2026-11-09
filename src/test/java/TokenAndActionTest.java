import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
@Feature("Проверка входных данных: Токен и Action")
public class TokenAndActionTest extends BaseTest{
    @Test
    @Story("Токен")
    @DisplayName("Запрос без токена отклоняется")
    @Description("В запросе нет поля token. Приложение должно вернуть ошибку.")
    void withoutToken(){
        authReturn(200);
    given()
            .baseUri(APP_URL)
            .header("X-Api-Key", API_KEY)
            .contentType("application/x-www-form-urlencoded")
            .formParam("action", "LOGIN")
            .when()
            .post("/endpoint")
            .then()
            .body("result", equalTo("ERROR"));

    }
    @ParameterizedTest(name = "Токен «{0}» отклоняется")
    @ValueSource(strings = {" ", "4K9V7X2N1W8B5Z3D6M0P9Q1R7S8T5U6", "CXNU2G573V8WBUKD3357G1A3D5YF58I5Y"})
    @Story("Токен")
    @Description("Токен неверной длины или пустой токен должен быть отклонён.")
    void invalidTokenRejected(String invalidToken){
        authReturn(200);
        send(invalidToken, "LOGIN")
                .then()
                .body("result", equalTo("ERROR"));
    }

    @Test
    @Story("Токен")
    @DisplayName("Корректный токен принимается")
    @Description("Токен из 32 символов (цифры и буквы A-F) принимается, вход проходит успешно.")
    void ValidTokenIsAccepted(){
        authReturn(200);
        send(randToken(), "LOGIN")
                .then()
                .body("result", equalTo("OK"));
    }
    @Test
    @Story("Действие")
    @DisplayName("Запрос без действия отклоняется")
    @Description("В запросе нет поля action. Приложение должно вернуть ошибку.")
    void withoutAction(){
        authReturn(200);
        doActionReturn(200);
        given()
                .baseUri(APP_URL)
                .header("X-Api-Key", API_KEY)
                .contentType("application/x-www-form-urlencoded")
                .formParam("token", randToken())
                .when()
                .post("/endpoint")
                .then()
                .body("result", equalTo("ERROR"));
    }
    @ParameterizedTest(name = "Действие «{0}» отклоняется")
    @ValueSource(strings = {" ", "REGISTER", "login"})
    @Story("Действие")
    @Description("Неизвестное действие должно быть отклонено.")
    void invalidActionRejected(String invalidAction){
        authReturn(200);
        doActionReturn(200);
        send(randToken(), invalidAction)
                .then()
                .log().all()
                .body("result", equalTo("ERROR"));
    }
}
