package com.zendesk.jazon.spock

import com.zendesk.jazon.MatchResult
import com.zendesk.jazon.MatcherFactory
import com.zendesk.jazon.actual.GsonActualFactory
import com.zendesk.jazon.actual.ObjectsActualFactory
import com.zendesk.jazon.expectation.SpockExpectationFactory

class JazonSpockAdapter {
    private static final GsonActualFactory GSON_ACTUAL_FACTORY = new GsonActualFactory();
    private static final MatcherFactory MATCHER_FACTORY = new MatcherFactory(
            new SpockExpectationFactory(),
            new ObjectsActualFactory()
    );
    private final String json

    private JazonSpockAdapter(String json) {
        this.json = json
    }

    static JazonSpockAdapter jazon(String json) {
        return new JazonSpockAdapter(json)
    }

    boolean matches(Map jsonAsMap) {
        return match(jsonAsMap)
    }

    boolean matches(List jsonAsList) {
        return match(jsonAsList)
    }

    private boolean match(Object expected) {
        MatchResult matchResult = MATCHER_FACTORY.matcher()
                .expected(expected)
                .actual(GSON_ACTUAL_FACTORY.actual(json))
                .match()
        if (matchResult.ok()) {
            return true
        }
        throw new AssertionError(errorMessage(matchResult))
    }

    private static GString errorMessage(MatchResult matchResult) {
        "\n-----------------------------------\nJSON MISMATCH:\n${matchResult.message()}\n-----------------------------------\n"
    }
}
