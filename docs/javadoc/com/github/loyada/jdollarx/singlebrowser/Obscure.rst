.. java:import:: com.github.loyada.jdollarx Images

.. java:import:: com.github.loyada.jdollarx InBrowser

.. java:import:: com.github.loyada.jdollarx Path

.. java:import:: java.util List

Obscure
=======

.. java:package:: com.github.loyada.jdollarx.singlebrowser
   :noindex:

.. java:type:: public class Obscure extends Images.Obscure implements AutoCloseable

   class that allows to hide elements temporarily. This is useful when doing visual testing, while ignoring elements that are not interesting for the test. For example - testing a chart while ignoring certain labels. This is an Autocloseable: it reverts to the original state when leaving the try{} block.

Constructors
------------
Obscure
^^^^^^^

.. java:constructor:: public Obscure(Path element)
   :outertype: Obscure

   Make the first element matching the given path temporarily hidden. If the element is not found, it ignores it.

   :param element: the path of the element to obscure

Obscure
^^^^^^^

.. java:constructor:: public Obscure(List<Path> elements)
   :outertype: Obscure

   Make the elements matching the given paths temporarily hidden. In case there are multiple matches, it will hide the first one. If the element is not found, it ignores it.

   :param elements: the elements to obscure

Obscure
^^^^^^^

.. java:constructor:: public Obscure(List<Path> elements, boolean strict)
   :outertype: Obscure

   Make the elements matching the given paths temporarily hidden. In case there are multiple matches, it will hide the first one.

   :param elements: the elements to obscure
   :param strict: in strict mode, if the element is not found, it throws am exception and stops

