package com.sn.playwright;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.Options;
import com.microsoft.playwright.junit.OptionsFactory;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@UsePlaywright(AnnotatedTest.MyOptions.class)
public class AnnotatedTest {

    public static class MyOptions implements OptionsFactory {

        @Override
        public Options getOptions() {
            return new Options()
                    .setHeadless(false)
                    .setLaunchOptions(
                            new BrowserType.LaunchOptions()
                                    .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
                    );
        }
    }

    @Test
    void shouldShowPageTitle(Page page){
        page.navigate("https://practicesoftwaretesting.com");

        String title = page.title();
        Assertions.assertTrue(title.contains("Practice Software Testing"));

    }

    @Test
    void shouldSearchByKeyword(Page page){
        page.navigate("https://practicesoftwaretesting.com");

        page.locator("[placeholder=Search]").fill("Pliers");
        page.locator("button:has-text('Search')").click();

        int matchingSearchResult = page.locator(".card").count();
        Assertions.assertTrue(matchingSearchResult > 0);

    }
}