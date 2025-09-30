package com.sn.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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
                        .setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
        browserContext = browser.newContext();
        playwright.selectors().setTestIdAttribute("data-test");
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
        org.junit.jupiter.api.Assertions.assertTrue(title.contains("Practice Software Testing"));

    }

    @Test
    void shouldSearchByKeyword(){
        page.navigate("https://practicesoftwaretesting.com");

        page.locator("[placeholder=Search]").fill("Pliers");
        page.locator("button:has-text('Search')").click();

        int matchingSearchResult = page.locator(".card").count();
        org.junit.jupiter.api.Assertions.assertTrue(matchingSearchResult > 0);

    }

    @Test
    void filteringSearchResult( ){
        page.navigate("https://practicesoftwaretesting.com");

        page.getByPlaceholder("Search").fill("Pliers");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

        assertThat(page.locator(".card")).hasCount(4);

        List<String> productNames = page.getByTestId("product-name").allTextContents();
        Assertions.assertThat(productNames).allMatch(name -> name.contains("Pliers"));

        Locator outOfStock = page.locator(".card")
                .filter(new Locator.FilterOptions().setHasText("Out of stock"))
                .getByTestId("product-name");
        assertThat(outOfStock).hasCount(1);
        assertThat(outOfStock).hasText("Long Nose Pliers");
    }
}