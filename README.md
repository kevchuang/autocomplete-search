# autocomplete-search

This project is an autocomplete similar to Google Autocomplete in Scala.

## Prerequisites

To run this project, you need to install [Java 8 JDK](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html) and [SBT](https://docs.scala-lang.org/getting-started/sbt-track/getting-started-with-scala-and-sbt-on-the-command-line.html).

## Running the tests

To run the unit tests, execute the command line below in a sbt shell:
```
test
```

## Additional Questions
### What would you change if the list of keywords was much larger (300 Gb) ?
Assuming the list of keywords was stored in ElasticSearch, I would use Completion Suggester which allow to implement a "search as you type" functionality. It creates an in-memory structure called Finite State Transcuder that give us a response under 1ms.

### What would you change if the requirements were to match any portion of thekeywords (for example, given the string “pro”, the program could suggest the keyword “re​pro​be”) ?
In the class TreeNode, I would create a string that stores all the previous nodes characters. And when the function, that is reading through the whole tree, finds a match with the string that contains previous nodes character, it will go through the next nodes until reaching a node that got isWord == true.
