# oscar-api
Experiments with spring and the OSCAR/Surface API

A spring boot project with a Spring and JPA, Hibernate and XSLT based approach to the OSCAR/Surface API and WIGOS metadata standard

## Capabilities
 - JPA based database configuration. No schema required
 - JSON, XML and WMDR data import and export
 - transformation from and to WMDR based on flexible XSLT templates
 - schema validation
 - leverages existing (spring) components where possibe.. little code
 - unit tests
 - supported operations: list, read, write, delete

## Installation

1. download STS (Eclipse)
2. import repository
3. run as "Spring Boot App"

## TODO
- schema validation for "native" simple XML format
- add "update" operation
