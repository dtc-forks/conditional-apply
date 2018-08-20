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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class ParameterizedIfPassedApplier<T, R> implements ParameterizedPassedApplier<T, R> {

    private final Map<Collection<Predicate<T>>, Function<T, R>> filterPredicatesToPassedFunction;

    ParameterizedIfPassedApplier(Map<Collection<Predicate<T>>, Function<T, R>> filterPredicatesToPassedFunction) {
        if (filterPredicatesToPassedFunction.isEmpty()) {
            throw new IllegalStateException("Does not make sense to create an empty instance of '" + ParameterizedIfPassedApplier.class + '\'');
        }
        this.filterPredicatesToPassedFunction = new LinkedHashMap<>(filterPredicatesToPassedFunction);
    }

    public Optional<R> apply(T t) {
        return filterPredicatesToPassedFunction.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isEmpty() || entry.getKey().stream().allMatch(tPredicate -> tPredicate.test(t)))
                .map(Map.Entry::getValue)
                .map(trFunction -> trFunction.apply(t))
                .findAny();
    }
}
