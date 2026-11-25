package com.newtube.mobile.ui.common;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BlockedTitleFilterTest {
    @Test
    public void matchesWordsAndPhrasesIgnoringCaseAndAccents() {
        BlockedTitleFilter filter = new BlockedTitleFilter("fútbol, liga mx, relig*");
        assertTrue(filter.matches("FUTBOL en directo"));
        assertTrue(filter.matches("La Liga MX hoy"));
        assertTrue(filter.matches("Noticias religiosas"));
        assertFalse(filter.matches("obligado"));
        assertFalse(filter.matches("futbolista"));
        assertFalse(filter.matches("irreligioso"));
    }

    @Test
    public void ignoresEmptyTermsAndHandlesLiteralCharacters() {
        BlockedTitleFilter filter = new BlockedTitleFilter(" , *, c++, ");
        assertTrue(filter.matches("Tutorial de C++"));
        assertFalse(filter.matches("C"));
        assertFalse(filter.matches(null));
        assertFalse(new BlockedTitleFilter("").matches("cualquier título"));
    }
}
