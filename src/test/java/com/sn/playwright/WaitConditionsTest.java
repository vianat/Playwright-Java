package com.sn.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(ChromeOptions.class)
public class WaitConditionsTest {

    @BeforeEach
    void openHomePage(Page page) {
        page.navigate("https://practicesoftwaretesting.com");
        page.waitForSelector(".card-img-top");
    }

    @DisplayName("wait for product loading")
    @Test
    void waitForProductLoading(Page page){

        List<String> productNames = page.getByTestId("product-name").allTextContents();
        Assertions.assertThat(productNames).contains(" Pliers ", " Bolt Cutters ", " Hammer ");
    }

    @DisplayName("wait for card images")
    @Test
    void waitForCardImages(Page page){

        List<String> productImagesTitles = page.locator(".card-img-top").all()
                        .stream()
                        .map(img -> img.getAttribute("alt"))
                        .toList();
        Assertions.assertThat(productImagesTitles).contains("Combination Pliers", "Bolt Cutters", "Hammer");
    }

    @Nested
    class AutomaticWaits{
        @BeforeEach
        void openHomePage(Page page) {
            page.navigate("https://practicesoftwaretesting.com");
        }

        @DisplayName("wait for filter checkbox")
        @Test
        void waitForFilterCheckbox(Page page){

            var screwdriverFilter = page.getByLabel("Screwdriver");
            screwdriverFilter.click();

            assertThat(screwdriverFilter).isChecked();
        }

        @DisplayName("should filter by category")
        @Test
        void shouldFilterByCategory(Page page){

            page.getByRole(AriaRole.MENUBAR).getByText("Categories").click();
            page.getByRole(AriaRole.MENUBAR).getByText("Power Tools").click();

            page.waitForSelector(".card");

            // or explicit wait 2 sec
//            page.waitForSelector(".card", new Page.WaitForSelectorOptions()
//                    .setState(WaitForSelectorState.VISIBLE).setTimeout(2000));

            var filteredProducts = page.getByTestId("product-name").allInnerTexts();

            Assertions.assertThat(filteredProducts).contains("Sheet Sander");
        }
    }

    @Nested
    class WaitFoeElementsAppearAndDisappear{
        @BeforeEach
        void openHomePage(Page page) {
            page.navigate("https://practicesoftwaretesting.com");
        }

        @DisplayName("wait for filter checkbox")
        @Test
        void waitFoeElementsAppearAndDisappear(Page page){

            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            assertThat(page.getByRole(AriaRole.ALERT)).isVisible();
            assertThat(page.getByRole(AriaRole.ALERT)).hasText("Product added to shopping cart.");
        }
    }
}