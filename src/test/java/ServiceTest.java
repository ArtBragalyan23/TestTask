import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.Matchers.equalTo;
@Feature("Неполадки на стороне внешнего сервиса")
public class ServiceTest extends BaseTest{
    @ParameterizedTest(name = "Вход не удаётся, если внешний сервис отвечает {0}")
    @ValueSource(ints = {401, 403, 500})
    @Story("Сбой при входе")
    @Description("Внешний сервис отказал в авторизации. Приложение должно вернуть ошибку.")
    void loginFailsWhenAuthRejects(int statusCode){
    authReturn(statusCode);
    send(randToken(), "LOGIN")
            .then()
            .body("result", equalTo("ERROR"));
}
    @Test
    @Story("Сбой при входе")
    @DisplayName("После неудачного входа действие недоступно")
    @Description("Вход не удался, токен не должен запомниться. Действие после этого отклоняется.")
    void actionAfterFailedLoginRejected(){
    String token = randToken();
    authReturn(500);
    doActionReturn(200);
    send(token, "LOGIN");
    send(token, "ACTION")
            .then()
            .body("result", equalTo("ERROR"));
}
    @ParameterizedTest(name = "Действие не выполняется, если внешний сервис отвечает {0}")
    @ValueSource(ints = {401, 403, 500})
    @Story("Сбой при действии")
    @Description("Пользователь вошёл, но внешний сервис отказал в действии. Приложение должно вернуть ошибку.")
    void actionFailsWhenDoActionServiceRejects(int status) {
        String token = randToken();
        authReturn(200);
        doActionReturn(status);
        send(token, "LOGIN")
                .then()
                .body("result", equalTo("OK"));
        send(token, "ACTION")
                .then()
                .body("result", equalTo("ERROR"));
    }
}
