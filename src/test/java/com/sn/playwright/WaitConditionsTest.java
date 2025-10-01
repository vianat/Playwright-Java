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

}