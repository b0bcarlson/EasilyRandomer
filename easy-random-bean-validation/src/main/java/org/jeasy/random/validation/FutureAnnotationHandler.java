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
package org.jeasy.random.validation;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.jeasy.random.EasilyRandomer;
import org.jeasy.random.EasilyRandomerParameters;
import org.jeasy.random.api.Randomizer;

class FutureAnnotationHandler implements BeanValidationAnnotationHandler {

    private EasilyRandomer easilyRandomer;
    private EasilyRandomerParameters parameters;

    FutureAnnotationHandler(EasilyRandomerParameters parameters) {
        this.parameters = parameters.copy();
    }

    @Override
    public Randomizer<?> getRandomizer(Field field) {
        if (easilyRandomer == null) {
            LocalDate now = LocalDate.now();
            parameters.setDateRange(new EasilyRandomerParameters.Range<>(
                    now.plus(1, ChronoUnit.DAYS),
                    now.plusYears(EasilyRandomerParameters.DEFAULT_DATE_RANGE)
            ));
            easilyRandomer = new EasilyRandomer(parameters);
        }
        return () -> easilyRandomer.nextObject(field.getType());
    }
}
