package com.jsunsoft.conditionalapply;

/*
 * Copyright 2017 Benik Arakelyan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class ParameterizedIfPassedApplierBuilder<T, R> {
    private Collection<Predicate<T>> filterPredicates;

    private final Map<Collection<Predicate<T>>, Function<T, R>> filterSuppliersToPassedFunction;


    private ParameterizedIfPassedApplierBuilder() {
        filterPredicates = new ArrayList<>();
        filterSuppliersToPassedFunction = new LinkedHashMap<>();
    }

    public ParameterizedIfPassedApplierBuilder<T, R> filter(Predicate<T> filterPredicate) {
        Objects.requireNonNull(filterPredicate, "parameter 'filterPredicate' may not be null");

        filterPredicates.add(filterPredicate);
        return this;
    }

    public ParameterizedIfPassedApplierBuilder<T, R> ifPassedApply(Function<T, R> passedFunction) {
        Objects.requireNonNull(passedFunction, "parameter 'passedFunction' may not be null");
        if (filterPredicates.isEmpty()) {
            throw new IllegalStateException("Filter isn't present");
        }
        filterSuppliersToPassedFunction.put(filterPredicates, passedFunction);
        filterPredicates = new ArrayList<>();
        return this;
    }

    public ParameterizedIfPassedApplierBuilder<T, R> otherwise(Function<T, R> passedFunction) {
        filterSuppliersToPassedFunction.put(Collections.emptyList(), passedFunction);
        return this;
    }

    public ParameterizedIfPassedApplier<T, R> build() {
        return new ParameterizedIfPassedApplier<>(filterSuppliersToPassedFunction);
    }


    public static <T, R> ParameterizedIfPassedApplierBuilder<T, R> create() {
        return new ParameterizedIfPassedApplierBuilder<>();
    }
}
