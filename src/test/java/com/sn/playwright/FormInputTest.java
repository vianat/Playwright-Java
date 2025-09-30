package com.sn.playwright;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@UsePlaywright(ChromeOptions.class)
public class FormInputTest {

    @DisplayName("Form Input test")
    @Test
    void formInputTest(Page page) throws IOException, URISyntaxException {
        page.navigate("https://practicesoftwaretesting.com/contact");

        var firstNameField = page.getByLabel("First name");
        var lastNameField = page.getByLabel("Last name");
        var emailField = page.getByLabel("Email");
        var messageField = page.getByLabel("Message");
        var subjectField = page.getByLabel("Subject");
        var uploadField = page.getByLabel("Attachment");

        firstNameField.fill("Sara-Maria");
        lastNameField.fill("Smith");
        emailField.fill("Sara-Maria@google.com");
        messageField.fill("Hello, playwright!");
        subjectField.selectOption("Warranty");
//        subjectField.selectOption(new SelectOption().setIndex(2));
//        subjectField.selectOption(new SelectOption().setLabel("label"));
//        subjectField.selectOption(new SelectOption().setValue("value"));

        Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/upload.txt").toURI());
        page.setInputFiles("#attachment", fileToUpload);

        assertThat(firstNameField).hasValue("Sara-Maria");
        assertThat(lastNameField).hasValue("Smith");
        assertThat(emailField).hasValue("Sara-Maria@google.com");
        assertThat(messageField).hasValue("Hello, playwright!");
        assertThat(subjectField).hasValue("warranty");

        String uploadedFile = uploadField.inputValue();
        Assertions.assertThat(uploadedFile).endsWith("upload.txt");

    }

    //ddt from junit
    @DisplayName("Required fields")
    @ParameterizedTest()
    @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
    void mandatoryFields(String fieldName, Page page){
        page.navigate("https://practicesoftwaretesting.com/contact");

        var firstNameField = page.getByLabel("First name");
        var lastNameField = page.getByLabel("Last name");
        var emailField = page.getByLabel("Email");
        var messageField = page.getByLabel("Message");
        var subjectField = page.getByLabel("Subject");
        var sendButton = page.getByText("Send");

        firstNameField.fill("Sara-Maria");
        lastNameField.fill("Smith");
        emailField.fill("Sara-Maria@google.com");
        messageField.fill("Hello, playwright!");
        subjectField.selectOption("Warranty");

        page.getByLabel(fieldName).clear();

        sendButton.click();

        var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");
        assertThat(errorMessage).isVisible();
    }
}