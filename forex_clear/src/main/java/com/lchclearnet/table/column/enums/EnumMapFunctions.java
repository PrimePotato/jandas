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

import com.lchclearnet.table.Column;

/**
 * An interface for mapping operations unique to Currency columns
 */
public interface EnumMapFunctions<T extends Enum> extends Column<T> {
    int getIntInternal(int r);

    T get(int index);

    T min();

    T max();
}
