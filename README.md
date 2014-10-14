SpringClips
=================================

Yet another Spring Boot configuration integrated with EclipseLink

Basic Spring Boot configuration with some extras:

[Gradle]
- Wrapper support
- Build for jar and war bundles too (.gradlew jar war)
- Integrated PMD and JSHint for static code analyzing (./gradlew jshint pmdMain)
- IntelliJ IDEA integration (./gradlew idea)

[Spring]
- Full UTF-8 support
- Multipart accepted sizes
- Environment based application configuration (-Denv.name=test)
- Thymeleaf template engine
- Standalone and auto-register at Webcontext are supported too

[Eclipselink]
- Embedded Derby database
- Custom enum converters (see com.github.yasbc.domains.TestDomain)

[Logging]
- Custom Audit logging with com.github.yasbc.utils.Auditor
