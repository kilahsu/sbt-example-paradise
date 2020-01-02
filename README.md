A project modified from the [Macro Paradise Example](https://github.com/scalamacros/sbt-example-paradise) to demonstrate issue of reading class member annotations during macro expension

The macro expends a companion object based on the annotations added to the class.

When run with Scala 2.11.12, `sbt run` returns the following

```
[info] Running Test 
john: Sad
lisa: Happy, Laugh
ben: 
``` 

However, when running with Scala 2.12.10, `sbt run` returns the following

```
[info] Running Test 
john: 
lisa: 
ben:
```
