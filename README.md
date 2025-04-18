<div align="center">
<img src="https://github.com/quarkiverse/.github/blob/main/assets/images/quarkus.svg" width="67" height="70" ><img src="https://github.com/quarkiverse/.github/blob/main/assets/images/plus-sign.svg" height="70" ><img src="https://github.com/quarkiverse/quarkus-poi/blob/main/docs/modules/ROOT/assets/images/poi.svg" height="70" >

# Quarkus POI
</div>
<br>

[![Version](https://img.shields.io/maven-central/v/io.quarkiverse.poi/quarkus-poi?logo=apache-maven&style=flat-square)](https://search.maven.org/artifact/io.quarkiverse.poi/quarkus-poi)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](https://opensource.org/licenses/Apache-2.0)
[![Build](https://github.com/quarkiverse/quarkus-poi/actions/workflows/build.yml/badge.svg)](https://github.com/quarkiverse/quarkus-poi/actions/workflows/build.yml)

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

## Docker

When building native images in Docker using the standard Quarkus Docker configuration files some additional features need to be
installed to support Apache POI.  Specifically font information is not included in [Red Hat's ubi-minimal images](https://developers.redhat.com/products/rhel/ubi).  To install it
simply add these lines to your `DockerFile.native` file:

```shell
FROM registry.access.redhat.com/ubi9/ubi-minimal:9.5

######################### Set up environment for POI #############################
RUN microdnf update -y && microdnf install -y freetype fontconfig && microdnf clean all
######################### Set up environment for POI #############################

WORKDIR /work/
RUN chown 1001 /work \
    && chmod "g+rwX" /work \
    && chown 1001:root /work
# Shared objects to be dynamically loaded at runtime as needed,
COPY --chown=1001:root --chmod=0755 target/*.properties target/*.so /work/
COPY --chown=1001:root --chmod=0755 target/*-runner /work/application
# Permissions fix for Windows
RUN chmod "ugo+x" /work/application
EXPOSE 8080
USER 1001

CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]
```

> [!CAUTION]
> Make sure `.dockerignore` does not exclude `.so` files!

## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="http://gastaldi.wordpress.com"><img src="https://avatars.githubusercontent.com/u/54133?v=4?s=100" width="100px;" alt="George Gastaldi"/><br /><sub><b>George Gastaldi</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-poi/commits?author=gastaldi" title="Code">💻</a> <a href="#maintenance-gastaldi" title="Maintenance">🚧</a></td>
      <td align="center" valign="top" width="14.28%"><a href="http://tomscode.com"><img src="https://avatars.githubusercontent.com/u/896029?v=4?s=100" width="100px;" alt="Tomaž"/><br /><sub><b>Tomaž</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-poi/commits?author=tpodg" title="Code">💻</a> <a href="#maintenance-tpodg" title="Maintenance">🚧</a></td>
      <td align="center" valign="top" width="14.28%"><a href="http://melloware.com"><img src="https://avatars.githubusercontent.com/u/4399574?v=4?s=100" width="100px;" alt="Melloware"/><br /><sub><b>Melloware</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-poi/commits?author=melloware" title="Documentation">📖</a> <a href="https://github.com/quarkiverse/quarkus-poi/issues?q=author%3Amelloware" title="Bug reports">🐛</a> <a href="https://github.com/quarkiverse/quarkus-poi/commits?author=melloware" title="Code">💻</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/knuspertante"><img src="https://avatars.githubusercontent.com/u/32802753?v=4?s=100" width="100px;" alt="Michael Schilling"/><br /><sub><b>Michael Schilling</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-poi/issues?q=author%3Aknuspertante" title="Bug reports">🐛</a> <a href="https://github.com/quarkiverse/quarkus-poi/commits?author=knuspertante" title="Tests">⚠️</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/manofthepeace"><img src="https://avatars.githubusercontent.com/u/13215031?v=4?s=100" width="100px;" alt="manofthepeace"/><br /><sub><b>manofthepeace</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-poi/issues?q=author%3Amanofthepeace" title="Bug reports">🐛</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/gian1200"><img src="https://avatars.githubusercontent.com/u/405347?v=4?s=100" width="100px;" alt="Giancarlo Calderón Cárdenas"/><br /><sub><b>Giancarlo Calderón Cárdenas</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-poi/commits?author=gian1200" title="Documentation">📖</a></td>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/mschorsch"><img src="https://avatars.githubusercontent.com/u/4418363?v=4?s=100" width="100px;" alt="mschorsch"/><br /><sub><b>mschorsch</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-poi/issues?q=author%3Amschorsch" title="Bug reports">🐛</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!
