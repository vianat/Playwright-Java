package com.sn.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.junit.UsePlaywright;
import org.junit.jupiter.api.*;

import java.util.List;

@UsePlaywright(ChromeOptions.class)
public class SimpleFormTest {

    @Test
    void submitForm(Page page){
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