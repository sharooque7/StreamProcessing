package org.ainzson.oops.generics.interfaces;

public interface PipelineStage<T> {
    T process(T input);
}
