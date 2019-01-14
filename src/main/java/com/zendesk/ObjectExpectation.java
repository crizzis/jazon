package com.zendesk;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.zendesk.JazonMatchResult.failure;
import static java.util.Optional.empty;

public class ObjectExpectation implements JsonExpectation {
    private final Map<String, JsonExpectation> expectationMap;

    public ObjectExpectation(Map<String, JsonExpectation> expectationMap) {
        this.expectationMap = expectationMap;
    }

    @Override
    public JazonMatchResult match(ActualJsonNumber actualNumber) {
        return failure(new TypeMismatch(ActualJsonObject.class, ActualJsonNumber.class));
    }

    @Override
    public JazonMatchResult match(ActualJsonObject actualObject) {
        return matchMap(actualObject.map());
    }

    private JazonMatchResult matchMap(Map<String, Actual> jsonAsMap) {
        Optional<JsonMismatch> jsonMismatch = matchExpectedFields(jsonAsMap);
        return jsonMismatch.map(JazonMatchResult::failure)
                .orElseGet(JazonMatchResult::success);
    }

    private Optional<JsonMismatch> matchExpectedFields(Map<String, Actual> jsonAsMap) {
        for (Map.Entry<String, JsonExpectation> entry : expectationMap.entrySet()) {
            JsonExpectation fieldExpectation = entry.getValue();
            Actual actual = jsonAsMap.get(entry.getKey());
            JazonMatchResult matchResult = actual.accept(fieldExpectation);

            if (!matchResult.ok()) {
                return Optional.of(matchResult.mismatch());
            }
        }
        return empty();
    }

    @Override
    public JazonMatchResult match(ActualJsonString actualString) {
        return failure(new TypeMismatch(ActualJsonObject.class, ActualJsonString.class));
    }

    @Override
    public JazonMatchResult match(ActualJsonNull actualNull) {
        return failure(new NullMismatch<>(ActualJsonObject.class));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectExpectation that = (ObjectExpectation) o;
        return Objects.equals(expectationMap, that.expectationMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expectationMap);
    }

    @Override
    public String toString() {
        return "ObjectExpectation{" +
                "expectationMap=" + expectationMap +
                '}';
    }
}
