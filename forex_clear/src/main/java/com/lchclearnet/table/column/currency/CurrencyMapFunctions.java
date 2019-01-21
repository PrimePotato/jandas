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

package com.lchclearnet.table.column.currency;

import com.lchclearnet.table.Column;
import com.lchclearnet.utils.Currency;

/**
 * An interface for mapping operations unique to Currency columns
 */
public interface CurrencyMapFunctions extends Column<Currency> {
    int getIntInternal(int r);

    Currency get(int index);

    Currency min();

    Currency max();
}
