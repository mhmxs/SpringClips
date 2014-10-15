package com.github.springclips.domains;

import com.github.springclips.utils.converter.Convertable;
import com.github.springclips.utils.converter.TestConverter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class TestDomain {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @org.eclipse.persistence.annotations.Converter(name = "test", converterClass = TestConverter.class)
    @org.eclipse.persistence.annotations.Convert("test")
    private TestEnum test;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TestEnum getTest() {
        return test;
    }

    public void setTest(final TestEnum test) {
        this.test = test;
    }

    public static enum TestEnum implements Convertable<TestEnum> {
        TEST;

        @Override
        public Object convert() {
            return name().toString();
        }

        @Override
        public TestEnum getFromValue(final Object value) {
            return TestEnum.valueOf(value.toString());
        }
    }
}
