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

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class IfPassedApplierTest {
    private final String testValue1 = "test1";
    private final String testValue2 = "test2";

    @Test
    public void testPassed() {
        PassedApplier<String> ifPassedApplier =
                IfPassedApplierBuilder.<String>create()
                        .filter(() -> "test1".equals(testValue1))
                        .filter(() -> "test2".equals(testValue2))
                        .ifPassedApply(() -> "passed")
                        .build();

        Assert.assertEquals("passed", ifPassedApplier.apply().get());
    }

    @Test
    public void whenFirstConditionIsIncorrectTest() {
        PassedApplier<String> ifPassedApplier =
                IfPassedApplierBuilder.<String>create()
                        .filter(() -> "test1".equals(testValue1))
                        .filter(() -> "incorrect".equals(testValue2))
                        .ifPassedApply(() -> "never returned")
                        .filter(() -> "test1".equals(testValue1))
                        .filter(() -> "test2".equals(testValue2))
                        .ifPassedApply(() -> "passed")
                        .build();

        Assert.assertEquals("passed", ifPassedApplier.apply().get());
    }

    @Test
    public void otherwiseTest() {
        PassedApplier<String> ifPassedApplier =
                IfPassedApplierBuilder.<String>create()
                        .filter(() -> "test1".equals(testValue1))
                        .filter(() -> "incorrect".equals(testValue2))
                        .ifPassedApply(() -> "never returned")
                        .filter(() -> "incorrect".equals(testValue1))
                        .filter(() -> "test2".equals(testValue2))
                        .ifPassedApply(() -> "never returned")
                        .otherwise(() -> "passed")
                        .build();

        Assert.assertEquals("passed", ifPassedApplier.apply().get());
    }

    private ParameterizablePassedApplier<Map<String, String>, String> ifPassedApplier =
            ParameterizableIfPassedApplierBuilder.<Map<String, String>, String>create()
                    .filter((map1) -> map1.containsKey(testValue1))
                    .filter((map1) -> map1.containsValue(testValue2))
                    .ifPassedApply((map1) -> "passed")
                    .build();

    @Test
    public void testPassedParam() {
        Map<String, String> map = new HashMap<>();
        map.put("test1", "test2");


        Assert.assertEquals("passed", ifPassedApplier.apply(map).get());
    }
}
