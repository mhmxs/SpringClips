YASPC
=================================

Yet Another Spring Boot Configuration

Basic Spring Boot configuration with somw extras:

[Gradle]
- Wrapper support
- Build for jar and war bundles too (.gradlew jar war)
- Integrated PMD and JSHint for static code analyzing (./gradlew jshint pmdMain)
- IntelliJ IDEA integration (./gradlew idea)

[Spring]
- Full UTF-8 support
- Multipart accepted sizes
- Audit logging
- Environment based application configuration (-Denv.name=test)
- Thymeleaf template engine
- Standalone and auto-register at Webcontext are supported too

[Eclipselink]
- Embedded Derby database
- Custom enum converters (see com.github.yasbc.domains.TestDomain)
