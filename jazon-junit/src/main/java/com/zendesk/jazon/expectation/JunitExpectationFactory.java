package com.zendesk.jazon.expectation;

import com.zendesk.jazon.actual.ActualJsonBoolean;
import com.zendesk.jazon.actual.ActualJsonNumber;
import com.zendesk.jazon.actual.ActualJsonString;
import com.zendesk.jazon.junit.ObjectExpectationInput;
import com.zendesk.jazon.junit.PredicateExpectationInput;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import static com.zendesk.jazon.expectation.ExpectationFactory.expectedOrderedArray;
import static com.zendesk.jazon.expectation.ExpectationFactory.expectedUnorderedArray;
import static com.zendesk.jazon.expectation.ExpectationFactory.objectExpectation;

public class JunitExpectationFactory implements ExpectationFactory {

    @Override
    public JsonExpectation expectation(Object object) {
        if (object instanceof Map) {
            return objectExpectation((Map<String, Object>) object, this);
        } else if (object instanceof Number) {
            return new PrimitiveValueExpectation<>(new ActualJsonNumber((Number) object));
        } else if (object instanceof String) {
            return new PrimitiveValueExpectation<>(new ActualJsonString((String) object));
        } else if (object instanceof Boolean) {
            return new PrimitiveValueExpectation<>(new ActualJsonBoolean((Boolean) object));
        } else if (object instanceof List) {
            return expectedOrderedArray((List<Object>) object, this);
        } else if (object instanceof Set) {
            return expectedUnorderedArray((Set<Object>) object, this);
        } else if (object == null) {
            return new NullExpectation();
        } else if (object instanceof Predicate) {
            return new PredicateExpectation((Predicate<Object>) object);
        } else if (object instanceof ObjectExpectationInput) {
            ObjectExpectationInput expectationInput = (ObjectExpectationInput) object;
            return expectation(expectationInput.object());
        } else if (object instanceof PredicateExpectationInput) {
            PredicateExpectationInput expectationInput = (PredicateExpectationInput) object;
            return new PredicateExpectation(expectationInput.predicate());
        }
        throw new IllegalArgumentException();
    }
}
