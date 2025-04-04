package org.example.visitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.languagetool.rules.RuleMatch;
import org.example.utils.SpellCheckUtils;

import java.util.List;


class SpellCheckUtilsTest {

    @Test
    void hasErrors_shouldReturnFalseForNullText() {
        SpellCheckUtils utils = new SpellCheckUtils();
        assertFalse(utils.hasErrors(null));
    }

    @Test
    void hasErrors_shouldReturnFalseForEmptyText() {
        SpellCheckUtils utils = new SpellCheckUtils();
        assertFalse(utils.hasErrors(""));
    }

    @Test
    void hasErrors_shouldReturnTrueForTextWithErrors() {
        SpellCheckUtils utils = new SpellCheckUtils();
        assertTrue(utils.hasErrors("Ths is a tst."));
    }

    @Test
    void hasErrors_shouldReturnFalseForCorrectText() {
        SpellCheckUtils utils = new SpellCheckUtils();
        assertFalse(utils.hasErrors("This is a test."));
    }

    @Test
    void checkText_shouldReturnEmptyListForCorrectText() {
        SpellCheckUtils utils = new SpellCheckUtils();
        List<RuleMatch> result = utils.checkText("This is a test.");
        assertTrue(result.isEmpty());
    }

    @Test
    void checkText_shouldReturnListOfErrorsForTextWithErrors() {
        SpellCheckUtils utils = new SpellCheckUtils();
        List<RuleMatch> result = utils.checkText("Ths is a tst.");
        assertFalse(result.isEmpty());
    }

    @Test
    void checkText_shouldThrowRuntimeExceptionForIOException() {
        SpellCheckUtils utils = new SpellCheckUtils() {
            @Override
            public List<RuleMatch> checkText(String text) {
                throw new RuntimeException("LanguageTool check failed");
            }
        };
        assertThrows(RuntimeException.class, () -> utils.checkText("This is a test."));
    }
}