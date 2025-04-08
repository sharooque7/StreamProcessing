package org.ainzson.oops.generics.validations;

import org.ainzson.oops.generics.interfaces.PipelineStage;

public class ValidationStage<T> implements PipelineStage<T> {
    @Override
    public T process(T input) {
        if(input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        return input;
    }
}
