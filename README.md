# DollarX

## What is it?
A library with the goal of making interactions with a web browser for the purpose 
of testing web applications trivial and fun.

## Properies
* Two versions: Scala(dollarX) and Java (jdollarX). Both are immutable.
* Flexible syntax that allows to declare complex DOM element easily.
     div withText "yes!" after span withClass "foo" 
* Optimized for atomic interaction with the browser, to avoid common pitfalls in testing web applications and optimize performance.
* Includes custom matchers for both Hamcrest and ScalaTest. The matchers provide useful error messages.
* Easy to troubleshoot, since toString() output is basically English.
* Check dollarx-example and jdollar-example for examples of working tests.

     