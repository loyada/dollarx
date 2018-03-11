.. java:import:: com.google.common.collect ImmutableList

.. java:import:: org.openqa.selenium WebElement

.. java:import:: java.util Arrays

.. java:import:: java.util Collections

.. java:import:: java.util List

.. java:import:: java.util Optional

.. java:import:: java.util.stream Collectors

.. java:import:: java.util.stream Stream

BasicPath.GlobalOccurrenceNumber
================================

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public static final class GlobalOccurrenceNumber
   :outertype: BasicPath

   Not to be used directly, but through the utility functions: \ :java:ref:`firstOccurrenceOf(Path)`\ , \ :java:ref:`lastOccurrenceOf(Path)`\ , \ :java:ref:`occurrenceNumber(Integer)`\

Constructors
------------
GlobalOccurrenceNumber
^^^^^^^^^^^^^^^^^^^^^^

.. java:constructor::  GlobalOccurrenceNumber(Integer n)
   :outertype: BasicPath.GlobalOccurrenceNumber

Methods
-------
of
^^

.. java:method:: public Path of(Path path)
   :outertype: BasicPath.GlobalOccurrenceNumber

   return the nth global occurrence (in the entire document) of the given path.

   :param path: the element to find
   :return: a new Path instance, that adds the global occurrence constraint to it

