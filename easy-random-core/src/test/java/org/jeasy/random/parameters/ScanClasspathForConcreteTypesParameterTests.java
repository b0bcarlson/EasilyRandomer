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
import static org.assertj.core.api.BDDAssertions.then;

import java.util.Date;

import org.assertj.core.api.Assertions;
import org.jeasy.random.EasilyRandomer;
import org.jeasy.random.EasilyRandomerParameters;
import org.junit.jupiter.api.Test;

import org.jeasy.random.ObjectCreationException;
import org.jeasy.random.beans.Ape;
import org.jeasy.random.beans.Bar;
import org.jeasy.random.beans.ClassUsingAbstractEnum;
import org.jeasy.random.beans.ComparableBean;
import org.jeasy.random.beans.ConcreteBar;
import org.jeasy.random.beans.Foo;
import org.jeasy.random.beans.Human;
import org.jeasy.random.beans.Mamals;
import org.jeasy.random.beans.Person;
import org.jeasy.random.beans.SocialPerson;

class ScanClasspathForConcreteTypesParameterTests {

    private EasilyRandomer easilyRandomer;

    @Test
    void whenScanClasspathForConcreteTypesIsDisabled_thenShouldFailToPopulateInterfacesAndAbstractClasses() {
        EasilyRandomerParameters parameters = new EasilyRandomerParameters().scanClasspathForConcreteTypes(false);
        easilyRandomer = new EasilyRandomer(parameters);

        assertThatThrownBy(() -> easilyRandomer.nextObject(Mamals.class)).isInstanceOf(ObjectCreationException.class);
    }

    @Test
    void whenScanClasspathForConcreteTypesIsEnabled_thenShouldPopulateInterfacesAndAbstractClasses() {
        EasilyRandomerParameters parameters = new EasilyRandomerParameters().scanClasspathForConcreteTypes(true);
        easilyRandomer = new EasilyRandomer(parameters);

        Mamals mamals = easilyRandomer.nextObject(Mamals.class);

        assertThat(mamals.getMamal()).isOfAnyClassIn(Human.class, Ape.class, Person.class, SocialPerson.class);
        assertThat(mamals.getMamalImpl()).isOfAnyClassIn(Human.class, Ape.class, Person.class, SocialPerson.class);
    }

    @Test
    void whenScanClasspathForConcreteTypesIsEnabled_thenShouldPopulateConcreteTypesForFieldsWithGenericParameters() {
        EasilyRandomerParameters parameters = new EasilyRandomerParameters().scanClasspathForConcreteTypes(true);
        easilyRandomer = new EasilyRandomer(parameters);

        ComparableBean comparableBean = easilyRandomer.nextObject(ComparableBean.class);

        assertThat(comparableBean.getDateComparable()).isOfAnyClassIn(ComparableBean.AlwaysEqual.class, Date.class);
    }

    @Test
    void whenScanClasspathForConcreteTypesIsEnabled_thenShouldPopulateAbstractTypesWithConcreteSubTypes() {
        // Given
        EasilyRandomerParameters parameters = new EasilyRandomerParameters().scanClasspathForConcreteTypes(true);
        easilyRandomer = new EasilyRandomer(parameters);

        // When
        Bar bar = easilyRandomer.nextObject(Bar.class);

        // Then
        assertThat(bar).isNotNull();
        assertThat(bar).isInstanceOf(ConcreteBar.class);
        // https://github.com/j-easy/easy-random/issues/204
        assertThat(bar.getI()).isNotNull();
    }

    @Test
    void whenScanClasspathForConcreteTypesIsEnabled_thenShouldPopulateFieldsOfAbstractTypeWithConcreteSubTypes() {
        // Given
        EasilyRandomerParameters parameters = new EasilyRandomerParameters().scanClasspathForConcreteTypes(true);
        easilyRandomer = new EasilyRandomer(parameters);

        // When
        Foo foo = easilyRandomer.nextObject(Foo.class);

        // Then
        assertThat(foo).isNotNull();
        assertThat(foo.getBar()).isInstanceOf(ConcreteBar.class);
        assertThat(foo.getBar().getName()).isNotEmpty();
    }

    @Test
    void whenScanClasspathForConcreteTypesIsEnabled_thenShouldPopulateAbstractEnumeration() {
        EasilyRandomerParameters parameters = new EasilyRandomerParameters().scanClasspathForConcreteTypes(true);
        easilyRandomer = new EasilyRandomer(parameters);

        ClassUsingAbstractEnum randomValue = easilyRandomer.nextObject(ClassUsingAbstractEnum.class);

        then(randomValue.getTestEnum()).isNotNull();
    }

    // issue https://github.com/j-easy/easy-random/issues/353

    @Test
    void testScanClasspathForConcreteTypes_whenConcreteTypeIsAnInnerClass() {
        EasilyRandomerParameters parameters =
                new EasilyRandomerParameters().scanClasspathForConcreteTypes(true);
        EasilyRandomer easilyRandomer = new EasilyRandomer(parameters);

        Foobar foobar = easilyRandomer.nextObject(Foobar.class);

        Assertions.assertThat(foobar).isNotNull();
        Assertions.assertThat(foobar.getToto()).isNotNull();
    }

    public class Foobar {

        public abstract class Toto {}

        public class TotoImpl extends Toto {}

        private Toto toto;

        public Toto getToto() {
            return toto;
        }
    }

}
