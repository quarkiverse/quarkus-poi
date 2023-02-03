# Quarkus extension for Apache POI

[![Version](https://img.shields.io/maven-central/v/io.quarkiverse.poi/quarkus-poi?logo=apache-maven&style=flat-square)](https://search.maven.org/artifact/io.quarkiverse.poi/quarkus-poi)

This is a Quarkus extension for Apache POI.

## What is Apache POI?

[Apache POI](https://poi.apache.org) is a Java API for Microsoft Documents. It allows Java programs to read and write files in Microsoft Office formats, such as Word, Excel and PowerPoint. It also allows programs to create new files in these formats. It is an open source project and is available under the Apache License.

Apache POI is a project of the Apache Software Foundation.

## What is Quarkus?

Quarkus is a Kubernetes Native Java stack tailored for GraalVM & OpenJDK HotSpot, crafted from the best of breed Java libraries and standards.

## How to use this extension?

Include the following dependency in your pom.xml:

```xml
<dependency>
    <groupId>io.quarkiverse.poi</groupId>
    <artifactId>quarkus-poi</artifactId>
    <version>${quarkus.poi.version}</version>
</dependency>
```

## Features

This extension provides the following features:

- Native image support
- Support for Apache POI 5.2.3