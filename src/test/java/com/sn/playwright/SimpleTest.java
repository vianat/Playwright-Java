package com.sn.playwright;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.lang.reflect.Array;
import java.util.Arrays;

public class SimpleTest{

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;
    Page page;

    @BeforeAll
    public static void setupBrowser(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
        browserContext = browser.newContext();
    }

    @BeforeEach
    public void beforeEach(){
        page = browserContext.newPage();
    }

    @AfterAll
    public static void tearDown(){
        browser.close();
        playwright.close();
    }

    @Test
    void shouldShowPageTitle( ){
        page.navigate("https://practicesoftwaretesting.com");

        String title = page.title();
        Assertions.assertTrue(title.contains("Practice Software Testing"));

    }

    @Test
    void shouldSearchByKeyword(){
        page.navigate("https://practicesoftwaretesting.com");

        page.locator("[placeholder=Search]").fill("Pliers");
        page.locator("button:has-text('Search')").click();

        int matchingSearchResult = page.locator(".card").count();
        Assertions.assertTrue(matchingSearchResult > 0);

    }
}