package kz.services.epicgamesfreegamebot.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


@ExtendWith(MockitoExtension.class)
public class TranslateServiceTest {

    @InjectMocks
    private TranslateService translateService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(translateService, "FOLDER_ID", "b1gj72eslelbhlkkpbkl");
        ReflectionTestUtils.setField(translateService, "API_URL", "https://translate.api.cloud.yandex.net/translate/v2/translate");
    }

    @Test
    public void translateTest() {
        String translate = translateService.translateText(new String[]{"Hello, world!"});
        Assertions.assertEquals("Привет, мир!", translate);
    }
}