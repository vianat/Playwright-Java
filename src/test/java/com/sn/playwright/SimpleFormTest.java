package com.sn.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

public class SimpleFormTest {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;
    Page page;

    @BeforeAll
    public static void setupBrowser(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(false)
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
    void submitForm(){
        page.navigate("https://practicesoftwaretesting.com/contact");

        page.locator("#first_name").fill("Sara-Parker");
        PlaywrightAssertions.assertThat(page.locator("#first_name")).hasValue("Sara-Parker");

        page.locator(".btnSubmit").click();

        List<String> alertMesseges = page.locator(".alert").allTextContents();
        Assertions.assertTrue(!alertMesseges.isEmpty());

        page.locator("[placeholder='Your last name *']").fill("Smith");
        PlaywrightAssertions.assertThat(page.locator("#last_name")).hasValue("Smith");
    }
}