[![][travis img]][travis]
[![][license img]][license]
##### DollarX Java [![][maven-java img]][maven-java]
##### DollarX Scala [![][maven-scala img]][maven-scala]
# DollarX

## What is it?
A library with the goal of making interactions with a web browser for the purpose 
of testing web applications trivial and fun.

## Properies
* Two versions: Scala(dollarX) and Java (jdollarX). Both are immutable.
* Flexible syntax that allows to declare arbitrarily complex DOM elements easily.
* Optimized for atomic interaction with the browser, to avoid common pitfalls in testing web applications and optimize performance.
* Includes custom matchers for both Hamcrest and ScalaTest. The matchers provide useful error messages.
* Easy to troubleshoot, since toString() output is basically English.
* Two "flavors": The standard, with multiple browsers instances support, and a simplified API for a single browser instance.
* Check dollarx-example and jdollar-example for examples of working tests.

##Examples

Declare DOM element as you typically describe them in English. Debug them easily since their description is English-like.
The DSL allows to build arbitrarily complex definitions easily.
```java
  val dialog = div withClass "ui-dialog" withText "foo"
  println(dialog)
  // div, that has class "ui-dialog", and has the text "foo"

  val myButton = button withClass "foo" that((has textContaining "submit")) inside dialog
  println(myButton)
  // button, that has class "foo", and (has text containing "submit"), inside (div, that has class "ui-dialog", and has the text "foo")

  val shoppingCart = dialog describedBy "\"shopping cart\" dialog"
  println(shoppingCart withClasses ("foo", "bar"))
  // "shopping cart" dialog, that has classes [foo, bar]

  val buyButton = button inside shoppingCart that(has text "buy!")
  println(buyButton)
  // button, inside ("shopping cart" dialog), and has the text "buy!"

  println(div that( has no cssClass("foo", "bar")) or (span withClass "moo"))
  //(div, that has non of the classes: [foo, bar]) or (span, that has class "moo")

  val name = listItem withClass "first-name" describedBy "first name entry"
  println(!(name that(has text "Danny")))
  // anything except (first name entry, that has the text "Danny")

  val myEntry = name that(has text "Jason") describedBy "Jason's entry"
  println(name that (is siblingOf myEntry))
  // first name entry, that has sibling: Jason's entry
```

No need to remember a specific API or a brittle css/xpath. The grammar is flexible. Code it the way you think about it.
EASY! 
```java
  val dialog = div withClass "ui-dialog"

  // All the following expressions are equivalent:
  val row = has cssClass "abcd" inside dialog
  val row = has cssClass "abcd" and (is inside dialog)
  val row = has cssClass "abcd" and (is containedIn dialog)
  val row = has cssClass "abcd" and (is descendantOf dialog)
  val row = has cssClass "abcd" and (has ancestor dialog)

  val row = element inside dialog  withClass "abcd"
  val row = is inside dialog withClass "abcd"
  val row = element withClass "abcd" inside dialog

  val row = element withClass "abcd" descendantOf dialog
  val row = element that(has cssClass "abcd") inside dialog
  val row = element that(has cssClass "abcd", has ancestor dialog)
  val row = element that((has cssClass "abcd") and (has ancestor dialog))

  val row = element that(has cssClass "abcd") and (has ancestor dialog)
```


### Behavior test examples instructions
For the behavior tests examples (in dollarx-example, jdollarx-example) to work, you need to
download the chrome selenium driver and set an environment variable 'CHROMEDRIVERPATH' to its path location.

##### maven dependency
[Just look here...](http://search.maven.org/#search%7Cga%7C1%7Cdollarx)

[travis]:https://travis-ci.org/loyada/dollarx
[travis img]:https://travis-ci.org/loyada/dollarx.svg?branch=master

[maven-scala]:http://search.maven.org/#search|gav|1|g:"com.github.loyada.dollarx"%20AND%20a:"dollarx-scala"
[maven-scala img]:https://maven-badges.herokuapp.com/maven-central/com.github.loyada.dollarx/dollarx-scala/badge.svg

[maven-java]:http://search.maven.org/#search|gav|1|g:"com.github.loyada.dollarx"%20AND%20a:"dollarx-java"
[maven-java img]:https://maven-badges.herokuapp.com/maven-central/com.github.loyada.dollarx/dollarx-java/badge.svg

[license]:LICENSE.txt
[license img]:https://img.shields.io/badge/License-Apache%202-blue.svg