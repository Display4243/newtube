package com.newtube.mobile.ui.common;

import android.content.Context;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public final class BlockedTitleFilter {
    private static final String PREFS = "blocked_title_filter";
    private static final String TERMS = "terms";
    private static final String WORD = "[\\p{L}\\p{N}_]";
    private final List<Pattern> patterns = new ArrayList<>();

    public BlockedTitleFilter(String terms) {
        if (terms == null) {
            return;
        }
        for (String term : terms.split(",")) {
            String normalized = normalize(term.trim());
            boolean prefix = normalized.endsWith("*");
            if (prefix) {
                normalized = normalized.substring(0, normalized.length() - 1).trim();
            }
            if (!normalized.isEmpty()) {
                patterns.add(Pattern.compile("(?<!" + WORD + ")" + Pattern.quote(normalized)
                        + (prefix ? WORD + "*" : "") + "(?!" + WORD + ")"));
            }
        }
    }

    public static String getTerms(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(TERMS, "");
    }

    public static void setTerms(Context context, String terms) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putString(TERMS, terms).apply();
    }

    public boolean matches(CharSequence title) {
        if (title == null || patterns.isEmpty()) {
            return false;
        }
        String normalized = normalize(title.toString());
        for (Pattern pattern : patterns) {
            if (pattern.matcher(normalized).find()) {
                return true;
            }
        }
        return false;
    }

    private static String normalize(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "").toLowerCase(Locale.ROOT);
    }
}
