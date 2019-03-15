# TrueDiff

I had trouble finding a library that really compares two POJO's that are more
document based that tabular based.

I tried adding history to an application I was working on using:

* [Hibernate Envers](http://hibernate.org/orm/envers/).
  Envers is nice if your data is tabular.

* [JaVers](https://javers.org/).
  Nice library/framework but has the same goal as Envers.

* [java-object-diff](https://github.com/SQiShER/java-object-diff).
  This is a very nice and feature rich library but it has a bug that is not being
fixed. The library also has too much features making it so large and complex that
the bug can not be resolved easily.

## Goals

To make a simple and feature rich Java Object Diff tool. But not too complex; just
enough to work and be understandable.

## Features

* Comparing objects should be one method call.
* Result should be a list of differences showing the type, property name, property value and property path.

## Details of comparing

### List

Since we compare stuff using equals comparing lists will only result in additions and deletions.
