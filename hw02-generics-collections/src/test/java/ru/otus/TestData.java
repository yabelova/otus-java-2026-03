package ru.otus;

public interface TestData {

    record PlanetExpressCrew(int id, String name) {
    }

    default Integer[] getIntArray() {
        return new Integer[]{11, 13, 17};
    }

    default String[] getStringArray() {
        return new String[]{"fu", "tu", "ra", "ma"};
    }

    default PlanetExpressCrew[] getCrewArray() {
        return new PlanetExpressCrew[]{
                new PlanetExpressCrew(1, "Leela"),
                new PlanetExpressCrew(2, "Fry"),
                new PlanetExpressCrew(3, "Bender")
        };
    }

    default String[] getWordsArray() {
        return new String[]{
                "Calculon", "Drama", "Amnesia", "Calculon", "Explosion",
                "Drama", "Coma", "Amnesia", "Calculon", "Drama",
                "Explosion", "Coma", "Calculon", "Drama", "Amnesia",
                "Explosion", "Calculon", "Drama", "Wedding", "Calculon"
        };
    }

}