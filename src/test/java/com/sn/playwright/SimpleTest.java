package com.sn.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import org.junit.jupiter.api.*;

import java.util.Comparator;
import java.util.List;
import org.assertj.core.api.Assertions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(ChromeOptions.class)
public class SimpleTest{

    @BeforeEach
    void openHomePage(Page page) {
        page.navigate("https://practicesoftwaretesting.com");
    }

    @DisplayName("Search for pliers")
    @Test
    void shouldShowPageTitle(Page page){

        String title = page.title();
        org.junit.jupiter.api.Assertions.assertTrue(title.contains("Practice Software Testing"));

    }

    @DisplayName("Check search result")
    @Test
    void shouldSearchByKeyword(Page page){

        page.locator("[placeholder=Search]").fill("Pliers");
        page.locator("button:has-text('Search')").click();

        int matchingSearchResult = page.locator(".card").count();
        org.junit.jupiter.api.Assertions.assertTrue(matchingSearchResult > 0);

    }

    @DisplayName("filter search result")
    @Test
    void filterSearchResult(Page page){

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

    @DisplayName("Check all product on page prices")
    @Test
    void allProductPricesShouldBeCorrectValues(Page page) {
        page.waitForCondition(() -> page.getByTestId("product-name").count() > 0);
        List<Double> prices = page.getByTestId("product-price")
                .allInnerTexts()
                .stream()
                .map(price -> Double.parseDouble(price.replace("$", "")))
                .toList();

        Assertions.assertThat(prices)
                .isNotEmpty()
                .allMatch(price -> price > 0)
                .doesNotContain(0.0)
                .allMatch(price -> price < 1000)
                .allSatisfy(price ->
                        Assertions.assertThat(price)
                                .isGreaterThan(0.0)
                                .isLessThan(1000.0));
    }

    @DisplayName("Check sorting a-z")
    @Test
    void alphabeticalSorting(Page page) {
        page.getByLabel("Sort").selectOption("Name (A - Z)");
//        page.waitForCondition(LoadState.NETWORKIDLE);

        List<String> productNames = page.getByTestId("product-name").allTextContents();

        Assertions.assertThat(productNames).isSortedAccordingTo(String.CASE_INSENSITIVE_ORDER);
    }

    @DisplayName("Check sorting z-a")
    @Test
    void alphabeticalSortingReverse(Page page) {
        page.getByLabel("Sort").selectOption("Name (Z - A)");
//        page.waitForCondition(LoadState.NETWORKIDLE);

        List<String> productNames = page.getByTestId("product-name").allTextContents();

        Assertions.assertThat(productNames).isSortedAccordingTo(Comparator.reverseOrder());
    }


}