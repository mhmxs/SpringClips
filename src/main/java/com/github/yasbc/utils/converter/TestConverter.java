package com.github.yasbc.utils.converter;

import com.github.yasbc.domains.TestDomain;

public class TestConverter extends AbstractEnumConverter {
    @Override
    public TestDomain.TestEnum getConvertableEnum() {
        return TestDomain.TestEnum.TEST;
    }
}