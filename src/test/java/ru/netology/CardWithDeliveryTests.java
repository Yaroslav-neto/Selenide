package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class CardWithDeliveryTests {
    private String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    public void shouldSuccessCompleted() {
        Selenide.open("http://localhost:9999");
        $("[data-test-id='city'] input").setValue("Новосибирск");
        String planningDate = generateDate(4, "dd.MM.yyyy");
        $("[data-test-id='date'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        $("[data-test-id='notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(
                        text("Успешно!"),
                        text("Встреча успешно забронирована на " + planningDate))
                .shouldHave(exactText("Успешно! Встреча успешно забронирована на " + planningDate));
    }

    @Test
    public void shouldAutoCompleted() {
        Selenide.open("http://localhost:9999");

        $("[data-test-id='city'] input").setValue("Но");
        $$("span.menu-item__control").findBy(Condition.exactText("Новосибирск")).click();

        String planningDate = generateDate(7, "dd.MM.yyyy");
        $("[data-test-id='date'] button").click();
        if (!generateDate(3, "MM").equals(generateDate(7, "MM")))
            $(".calendar__arrow_direction_right[data-step='1']").click();
        $$(".calendar__day").findBy(Condition.exactText(generateDate(7, "d"))).click();

        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("+79999999999");
        $("[data-test-id='agreement']").click();
        $("button.button").click();

        $("[data-test-id='notification']")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(
                        text("Успешно!"),
                        text("Встреча успешно забронирована на " + planningDate))
                .shouldHave(exactText("Успешно! Встреча успешно забронирована на " + planningDate));
    }
}
