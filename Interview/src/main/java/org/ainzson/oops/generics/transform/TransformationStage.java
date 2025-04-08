package org.ainzson.oops.generics.transform;

import org.ainzson.oops.generics.interfaces.PipelineStage;

public class TransformationStage implements PipelineStage<String> {
    @Override
    public String process(String input) {
        return  input.toUpperCase();
    }
}
