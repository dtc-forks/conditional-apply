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
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

class IfPassedApplier<R> implements PassedApplier<R> {
    private final Map<Collection<BooleanSupplier>, Supplier<R>> filterSuppliersToPassedSupplier;

    IfPassedApplier(Map<Collection<BooleanSupplier>, Supplier<R>> filterSuppliersToPassedSupplier) {
        if (filterSuppliersToPassedSupplier.isEmpty()) {
            throw new IllegalStateException(" Does not make sense to create an empty instance of '" + IfPassedApplier.class + '\'');
        }
        this.filterSuppliersToPassedSupplier = new LinkedHashMap<>(filterSuppliersToPassedSupplier);
    }

    public Optional<R> apply() {
        return filterSuppliersToPassedSupplier.entrySet()
                .stream()
                .filter(entry -> entry.getKey().isEmpty() || entry.getKey().stream().allMatch(BooleanSupplier::getAsBoolean))
                .map(Map.Entry::getValue)
                .map(Supplier::get)
                .findAny();
    }
}
