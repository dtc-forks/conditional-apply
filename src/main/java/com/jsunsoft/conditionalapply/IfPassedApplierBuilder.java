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
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class IfPassedApplierBuilder<R> {
    private Collection<BooleanSupplier> filterSuppliers;

    private final Map<Collection<BooleanSupplier>, Supplier<R>> filterSuppliersToPassedSupplier;


    private IfPassedApplierBuilder() {
        filterSuppliers = new ArrayList<>();
        filterSuppliersToPassedSupplier = new LinkedHashMap<>();
    }

    public IfPassedApplierBuilder<R> filter(BooleanSupplier filterSupplier) {
        Objects.requireNonNull(filterSupplier, "parameter 'filterSupplier' may not be null");

        filterSuppliers.add(filterSupplier);
        return this;
    }

    public IfPassedApplierBuilder<R> ifPassedApply(Supplier<R> passedSupplier) {
        Objects.requireNonNull(passedSupplier, "parameter 'passedSupplier' may not be null");

        if (filterSuppliers.isEmpty()) {
            throw new IllegalStateException("Filter isn't present");
        }
        filterSuppliersToPassedSupplier.put(filterSuppliers, passedSupplier);
        filterSuppliers = new ArrayList<>();
        return this;
    }

    public IfPassedApplierBuilder<R> otherwise(Supplier<R> passedSupplier) {
        Objects.requireNonNull(passedSupplier, "parameter 'passedSupplier' may not be null");

        filterSuppliersToPassedSupplier.put(Collections.emptyList(), passedSupplier);
        return this;
    }

    public IfPassedApplier<R> build() {
        return new IfPassedApplier<>(filterSuppliersToPassedSupplier);
    }


    public static <R> IfPassedApplierBuilder<R> create() {
        return new IfPassedApplierBuilder<>();
    }
}
