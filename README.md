[![][travis img]][travis]
[![][license img]][license]
##### DollarX Java
[![][maven-java img]][maven-java]
##### DollarX Scala
[![][maven-scala img]][maven-scala]
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