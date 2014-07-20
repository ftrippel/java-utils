java-utils
==========

Some very nice Java utility functions that I use on a regular basis

# Remarks

* If you do not want to import all maven dependencies of this project, e.g. the spring dependencies, you can easily block them

```maven
<dependency>
	<groupId>ftrippel.utils</groupId>
	<artifactId>utils</artifactId>
	<version>1.0-SNAPSHOT</version>
	<exclusions>
		<exclusion>
			<groupId>org.springframework</groupId>
			<artifactId>spring-core</artifactId>
		</exclusion>
		<exclusion>
			<groupId>org.springframework.batch</groupId>
	    	<artifactId>spring-batch-core</artifactId>
		</exclusion>
	</exclusions>
</dependency>
```
