/*
 * Source: https://github.com/OtusTeam/Java-Pro (Otus Java Pro course materials, lesson L25-di)
 */
package ru.otus.services;

public interface IOService {
    void out(String message);

    String readLn(String prompt);

    int readInt(String prompt);
}
