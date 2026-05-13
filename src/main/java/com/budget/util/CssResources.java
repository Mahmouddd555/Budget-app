package com.budget.util;

import java.net.URL;

/**
 * Central classpath URLs for application stylesheets (light / dark).
 */
public final class CssResources {

    private CssResources() {
    }

    public static URL lightTheme() {
        return CssResources.class.getResource("/css/light-theme.css");
    }

    public static URL darkTheme() {
        return CssResources.class.getResource("/css/dark-theme.css");
    }

    public static String lightThemeExternalForm() {
        URL u = lightTheme();
        return u != null ? u.toExternalForm() : null;
    }

    public static String darkThemeExternalForm() {
        URL u = darkTheme();
        return u != null ? u.toExternalForm() : null;
    }
}
