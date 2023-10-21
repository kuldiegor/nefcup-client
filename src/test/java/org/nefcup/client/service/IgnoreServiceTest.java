package org.nefcup.client.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IgnoreServiceTest {

    @Test
    void parsePatterns() {
        try (InputStream resourceStream = IgnoreServiceTest.class.getResourceAsStream("/org/nefcup/client/service/ignore/ignore.nefcup")) {
            String text = new String(resourceStream.readAllBytes(), StandardCharsets.UTF_8);
            List<String> patterns = IgnoreService.parsePatterns(text);
            assertEquals(5,patterns.size());
            assertLinesMatch(Arrays.asList(
                    "#sometext",
                    "temp/",
                    "temp",
                    ".gitignore",
                    "temp/123"
            ),patterns);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void isIgnore() {
        try (InputStream resourceStream = IgnoreServiceTest.class.getResourceAsStream("/org/nefcup/client/service/ignore/ignore.nefcup")) {
            String text = new String(resourceStream.readAllBytes(), StandardCharsets.UTF_8);
            IgnoreService ignoreService = new IgnoreService(text);
            /*
            * Проверка на стандартные строки вне зависимости от строк в ignore.nefcup*/
            assertTrue(ignoreService.isIgnore(Path.of("ignore.nefcup")));
            assertTrue(ignoreService.isIgnore(Path.of("nefcup.sh")));
            /*
            * Проверка на соотесвие строкам в ignore.nefcup*/
            assertTrue(ignoreService.isIgnore(Path.of("temp/123")));
            assertTrue(ignoreService.isIgnore(Path.of(".gitignore")));

            /*
             * Проверка на отсутвующие строки в ignore.nefcup и влияние регистра*/
            assertFalse(ignoreService.isIgnore(Path.of(".giTignore")));
            assertTrue(ignoreService.isIgnore(Path.of("temp/123/456")));
            assertTrue(ignoreService.isIgnore(Path.of("temp/1234")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createNefcupServiceWithNullPatternsText() {
        IgnoreService ignoreService = new IgnoreService(null);

        /*
         * Проверка на стандартные строки вне зависимости от строк в ignore.nefcup*/
        assertTrue(ignoreService.isIgnore(Path.of("ignore.nefcup")));
        assertTrue(ignoreService.isIgnore(Path.of("nefcup.sh")));

        /*
        * Так как на вход не был подан текст из ignore.nefcup
        * то все строки кроме стандартных не должны быть проигнорированы */
        assertFalse(ignoreService.isIgnore(Path.of("temp/123")));
        assertFalse(ignoreService.isIgnore(Path.of(".gitignore")));
        assertFalse(ignoreService.isIgnore(Path.of(".giTignore")));
        assertFalse(ignoreService.isIgnore(Path.of("temp/123/456")));
        assertFalse(ignoreService.isIgnore(Path.of("temp/1234")));
    }
}