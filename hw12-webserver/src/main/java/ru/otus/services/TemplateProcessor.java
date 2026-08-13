/*
 * Source: Java-Pro.zip (Otus Java Pro course materials, lesson L24-webServer)
 */
package ru.otus.services;

import java.io.IOException;
import java.util.Map;

public interface TemplateProcessor {
    String getPage(String filename, Map<String, Object> data) throws IOException;
}
