package com.sn.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;
import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(ChromeOptions.class)
public class WaitForAPICalls {

    @BeforeEach
    void openHomePage(Page page) {
        page.navigate("https://practicesoftwaretesting.com");
    }

    @DisplayName("wait for product loading")
    @Test
    void sortByDescendingPrice(Page page){

//        Sort by Descending price
        page.getByTestId("sort").selectOption("Price (High - Low)");
        page.getByTestId("product-price").first().waitFor();

//        Find all the prices on the page
        var productPrices = page.getByTestId("product-price")
                .allInnerTexts()
                .stream()
                .map(WaitForAPICalls::productPrices)
                .toList();

//        Are the prices in the correct order
        System.out.println("Product Prices: " + productPrices);
        Assertions.assertThat(productPrices)
                .isNotEmpty()
                .isSortedAccordingTo(Comparator.reverseOrder());
    }

    public static double productPrices(String price){
        return Double.parseDouble(price.replace("$",""));
    }
}