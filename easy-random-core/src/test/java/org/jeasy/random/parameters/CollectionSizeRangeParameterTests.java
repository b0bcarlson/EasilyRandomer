/*
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package org.jeasy.random.parameters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;

import org.jeasy.random.EasilyRandomer;
import org.jeasy.random.EasilyRandomerParameters;
import org.junit.jupiter.api.Test;

class CollectionSizeRangeParameterTests {

    @Test
    void shouldNotAllowNegativeMinCollectionSize() {
        assertThatThrownBy(() -> new EasilyRandomerParameters().collectionSizeRange(-1, 10)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldNotAllowMinCollectionSizeGreaterThanMaxCollectionSize() {
        assertThatThrownBy(() -> new EasilyRandomerParameters().collectionSizeRange(2, 1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void generatedCollectionSizeShouldBeInSpecifiedRange() {
        EasilyRandomerParameters parameters = new EasilyRandomerParameters().collectionSizeRange(0, 10);
        assertThat(new EasilyRandomer(parameters).nextObject(ArrayList.class).size()).isBetween(0, 10);
    }

    @Test // https://github.com/j-easy/easy-random/issues/191
    void collectionSizeRangeShouldWorkForArrays() {
        EasilyRandomerParameters parameters = new EasilyRandomerParameters().collectionSizeRange(0, 10);

        String[] strArr = new EasilyRandomer(parameters).nextObject(String[].class);

        assertThat(strArr.length).isLessThanOrEqualTo(10);
    }

}
