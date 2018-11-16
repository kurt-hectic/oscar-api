# oscar-api
Experiments with spring and the OSCAR/Surface API

A spring boot project with a Spring and JPA, Hibernate and XSLT based approach to the OSCAR/Surface API and WIGOS metadata standard

## Capabilities
 - JPA based database configuration. No schema required
 - JSON, XML and WMDR data import and export
 - transformation from and to WMDR based on flexible XSLT templates
 - schema validation
 - domain logic validation
 - leverages existing (spring) components where possibe.. little code
 - unit tests
 - supported operations: list, read, write, delete
 - detailed error reporting of XML and domain logic

## Installation

1. download STS (Eclipse)
2. import repository
3. run as "Spring Boot App"
4. allow network connections for schema download

## XML schema


```xml 
   <station>
	<name>Timo's test station</name>
	<established>2017-02-24</established>
	<observations>
		<observations>
			<variable>224</variable>
			<deployment>
				<beginning>2017-02-24T23:00:00.000+0000</beginning>
				<end/>
				<observationsource>automaticReading</observationsource>
				<schedules>
					<schedules>
						<monthfrom>12</monthfrom>
						<monthto>0</monthto>
						<dayfrom>1</dayfrom>
						<dayto>7</dayto>
						<hourfrom>0</hourfrom>
						<hourto>23</hourto>
						<minutefrom>0</minutefrom>
						<minuteto>59</minuteto>
						<interval>60</interval>
						<international>true</international>
					</schedules>
				</schedules>
			</deployment>
		</observations>
	</observations>
	<locations>
		<locations>
			<longitude>10.1</longitude>
			<latitude>20.2</latitude>
			<elevation>123.456</elevation>
			<datefrom>2017-02-24T23:00:00.000+0000</datefrom>
			<dateto/>
		</locations>
	</locations>
	<countryCode>DEU</countryCode>
	<region>Europe</region>
	<wigosID>
		<wigosID>0-20000-1-12334</wigosID>
		<primary>true</primary>
	</wigosID>
</station>
```

## TODO
- schema validation for "native" simple XML format
- add "update" operation
