import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalTo;
@Feature("Вход и действия пользователя")
public class ScenarioTest extends BaseTest {
    @Test
    @Story("Основной сценарий")
    @DisplayName("Вход, действие и выход проходят успешно")
    @Description("Пользователь входит, выполняет действие и выходит. Все три шага возвращают OK.")
    void actionWorks(){
        String token = randToken();
        authReturn(200);
        doActionReturn(200);
        send(token, "LOGIN").then().body("result", equalTo("OK"));
        send(token, "ACTION").then().body("result",equalTo("OK"));
        send(token, "LOGOUT").then().body("result", equalTo("OK"));
    }
    @Test
    @Story("Ограничения доступа")
    @DisplayName("Действие без входа отклоняется")
    @Description("Пользователь не входил в систему. Выполнить действие ему нельзя.")
    void actionWithoutLoginRejected(){
        doActionReturn(200);
        send(randToken(), "ACTION")
                .then()
                .body("result", equalTo("ERROR"));
    }
    @Test
    @Story("Ограничения доступа")
    @DisplayName("Действие после выхода отклоняется")
    @Description("Пользователь вошёл, потом вышел. После выхода выполнить действие нельзя.")
    void actionAfterLogoutRejected(){
        String token = randToken();
        authReturn(200);
        doActionReturn(200);
        send(token, "LOGIN");
        send(token, "LOGOUT");
        send(token, "ACTION")
                .then()
                .body("result",equalTo("ERROR"));
    }
}
