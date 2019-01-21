/*
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

package com.lchclearnet.table.column.enums;

import com.lchclearnet.table.predicates.IntBiPredicate;

import java.util.function.IntPredicate;

/**
 * Predicates for test DateColumn values and/or TimeColumn values
 * <p>
 * NOTE: These are not for testing DateTimeColumnValues, which are in the class DateTimePredicates
 */
public class EnumPredicates {

    public final static IntPredicate isMissing = i -> i == EnumColumn.MISSING_VALUE;

    public final static IntPredicate isNotMissing = i -> i != EnumColumn.MISSING_VALUE;

    public final static IntBiPredicate isGreaterThan = (valueToTest, valueToCompareAgainst) -> valueToTest > valueToCompareAgainst;

    public final static IntBiPredicate isGreaterThanOrEqualTo = (valueToTest, valueToCompareAgainst) -> valueToTest >= valueToCompareAgainst;

    public final static IntBiPredicate isLessThan = (valueToTest, valueToCompareAgainst) -> valueToTest < valueToCompareAgainst;

    public final static IntBiPredicate isLessThanOrEqualTo = (valueToTest, valueToCompareAgainst) -> valueToTest <= valueToCompareAgainst;

    public final static IntBiPredicate isEqualTo = (valueToTest, valueToCompareAgainst) -> valueToTest == valueToCompareAgainst;

    public final static IntBiPredicate isNotEqualTo = (valueToTest, valueToCompareAgainst) -> valueToTest != valueToCompareAgainst;
}
