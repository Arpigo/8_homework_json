package qa.guru;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class SelenideFileTest {
    @Test
    public void downloadTest() throws Exception { //базовый механизм как в джаве читаются файлы
        Selenide.open("https://github.com/junit-team/junit5/blob/main/README.md");
        File downloadedFile = Selenide.$("#raw-url").download();
        System.out.println("");
        try (InputStream is = new FileInputStream(downloadedFile)) {
        assertThat(new String(is.readAllBytes(), StandardCharsets.UTF_8)).contains("This repository is the home of the next generation of JUnit, _JUnit 5_.");
        }
    }

    @Test
    void uploadSelenideTest(){
        Selenide.open("https://the-internet.herokuapp.com/upload");
        Selenide.$("input[type='file']")//селектор для загружалки файлов,практически всегда стандартный
                .uploadFromClasspath("files/1.txt");
        Selenide.$("#file-submit").click();
        Selenide.$("div.example").shouldHave(Condition.text("File Uploaded!"));
        Selenide.$("#uploaded-files").shouldHave(Condition.text("1.txt"));
    }
}
