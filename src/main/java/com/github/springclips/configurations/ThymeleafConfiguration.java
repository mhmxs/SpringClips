package com.github.springclips.configurations;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.resourceresolver.SpringResourceResourceResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import javax.annotation.PostConstruct;
import javax.servlet.Servlet;
import java.util.Collection;
import java.util.Collections;

@Configuration
@ConditionalOnClass(SpringTemplateEngine.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class ThymeleafConfiguration {

    public static final String DEFAULT_PREFIX = "classpath:/templates/";

    public static final String DEFAULT_SUFFIX = ".html";

    @Configuration
    @ConditionalOnMissingBean(name = "defaultTemplateResolver")
    public static class DefaultTemplateResolverConfiguration implements EnvironmentAware {

        @Autowired
        private final ResourceLoader resourceLoader = new DefaultResourceLoader();

        private RelaxedPropertyResolver environment;

        @Override
        public void setEnvironment(final Environment environment) {
            this.environment = new RelaxedPropertyResolver(environment, "spring.thymeleaf.");
        }

        @PostConstruct
        public void checkTemplateLocationExists() {
            final Boolean checkLocation = this.environment.getProperty("checkTemplateLocation", Boolean.class, true);
            if (checkLocation) {
                final Resource resource = this.resourceLoader.getResource(this.environment.getProperty("prefix", DEFAULT_PREFIX));
                Assert.state(resource.exists(), "Cannot find template location: "
                        + resource + " (please add some templates "
                        + "or check your Thymeleaf configuration)");
            }
        }

        @Bean
        public ITemplateResolver defaultTemplateResolver() {
            final TemplateResolver resolver = new TemplateResolver();
            resolver.setResourceResolver(thymeleafResourceResolver());
            resolver.setPrefix(this.environment.getProperty("prefix", DEFAULT_PREFIX));
            resolver.setSuffix(this.environment.getProperty("suffix", DEFAULT_SUFFIX));
            resolver.setTemplateMode(this.environment.getProperty("mode", "HTML5"));
            resolver.setCharacterEncoding(this.environment.getProperty("encoding", "UTF-8"));

            final Boolean cache = this.environment.getProperty("cache", Boolean.class, true);
            resolver.setCacheable(cache);
            if (!cache) {
                resolver.setCacheTTLMs(0L);
            }

            return resolver;
        }

        @Bean
        protected SpringResourceResourceResolver thymeleafResourceResolver() {
            return new SpringResourceResourceResolver();
        }
    }

    @Configuration
    @ConditionalOnMissingBean(SpringTemplateEngine.class)
    protected static class ThymeleafDefaultConfiguration implements EnvironmentAware {

        @Autowired
        private final Collection<ITemplateResolver> templateResolvers = Collections
                .emptySet();
        @Autowired(required = false)
        private final Collection<IDialect> dialects = Collections.emptySet();
        private RelaxedPropertyResolver environment;

        @Override
        public void setEnvironment(final Environment environment) {
            this.environment = new RelaxedPropertyResolver(environment, "spring.thymeleaf.");
        }

        @Bean
        @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
        public SpringTemplateEngine templateEngine() {
            final SpringTemplateEngine engine = new SpringTemplateEngine();
            for (final ITemplateResolver templateResolver : this.templateResolvers) {
                engine.addTemplateResolver(templateResolver);
            }
            for (final IDialect dialect : this.dialects) {
                engine.addDialect(dialect);
            }
            if (!environment.getProperty("cache", Boolean.class, true)) {
                engine.setCacheManager(null);
            }
            return engine;
        }

    }

    @Configuration
    @ConditionalOnClass(name = "nz.net.ultraq.thymeleaf.LayoutDialect")
    protected static class ThymeleafWebLayoutConfiguration {

        @Bean
        public LayoutDialect layoutDialect() {
            return new LayoutDialect();
        }

    }

    @Configuration
    @ConditionalOnClass({Servlet.class})
    protected static class ThymeleafViewResolverConfiguration implements EnvironmentAware {

        private RelaxedPropertyResolver environment;
        @Autowired
        private SpringTemplateEngine templateEngine;

        @Override
        public void setEnvironment(final Environment environment) {
            this.environment = new RelaxedPropertyResolver(environment, "spring.thymeleaf.");
        }

        @Bean
        @ConditionalOnMissingBean(name = "thymeleafViewResolver")
        public ThymeleafViewResolver thymeleafViewResolver() {
            final ThymeleafViewResolver resolver = new ThymeleafViewResolver();
            resolver.setTemplateEngine(this.templateEngine);
            resolver.setCharacterEncoding(this.environment.getProperty("encoding", "UTF-8"));
            resolver.setContentType(appendCharset(this.environment.getProperty("contentType", "text/html"), resolver.getCharacterEncoding()));
            resolver.setExcludedViewNames(this.environment.getProperty("excludedViewNames", String[].class));
            resolver.setViewNames(this.environment.getProperty("viewNames", String[].class));
            resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 5);
            resolver.setCache(this.environment.getProperty("cache", Boolean.class, true));
            return resolver;
        }

        private String appendCharset(final String type, final String charset) {
            return type.contains("charset=") ? type : type + ";charset=" + charset;
        }
    }
}
