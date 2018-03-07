.. java:import:: java.util Optional

PathOperators
=============

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public final class PathOperators

Methods
-------
not
^^^

.. java:method:: public static Path not(Path path)
   :outertype: PathOperators

   Any element that does NOT conform to the definition of the given path parameters

   :param path: - the path that represent what the element does NOT match
   :return: a new path that represents the negation of the given parameter

