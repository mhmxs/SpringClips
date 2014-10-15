package com.github.springclips.utils.converter;

import com.github.springclips.domains.TestDomain;

public class TestConverter extends AbstractEnumConverter {
    @Override
    public TestDomain.TestEnum getConvertableEnum() {
        return TestDomain.TestEnum.TEST;
    }
}