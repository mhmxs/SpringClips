package com.github.yasbc.configurations;

import com.github.yasbc.filters.Logging;
import org.apache.catalina.filters.SetCharacterEncodingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class FilterConfiguration {

    @Bean
    public SetCharacterEncodingFilter getSetCharacterEncodingFilter() {
        final SetCharacterEncodingFilter filter = new SetCharacterEncodingFilter();
        filter.setEncoding("UTF-8");

        return filter;
    }

    @Bean
    public CharacterEncodingFilter getCharacterEncodingFilter() {
        final CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);

        return filter;
    }

    @Bean
    public Logging getLoggingFilter() {
        return new Logging();
    }
}
