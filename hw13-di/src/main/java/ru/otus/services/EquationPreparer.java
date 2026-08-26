/*
 * Source: https://github.com/OtusTeam/Java-Pro (Otus Java Pro course materials, lesson L25-di)
 */
package ru.otus.services;

import java.util.List;
import ru.otus.model.Equation;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
