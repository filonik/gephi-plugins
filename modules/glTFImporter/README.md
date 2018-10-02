## glTF Importer

Allows importing graphs from the glTF format. This facilitates the exchange of large graphs in a compact and portable binary representation.

### Dependencies

This plugin depends on [jgltf-browser](https://github.com/javagl/JglTF). 

A standalone jar can be dowloaded from:

https://github.com/javagl/JglTF/releases

And added to the local repository using:

```
mvn install:install-file -Dfile=jgltf-browser-2.0.0-SNAPSHOT-jar-with-dependencies-repackaged.jar -DgroupId=de.javagl -DartifactId=jgltf-browser -Dversion=2.0.0 -Dpackaging=jar
```
