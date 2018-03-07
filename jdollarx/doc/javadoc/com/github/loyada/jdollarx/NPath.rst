NPath
=====

.. java:package:: com.github.loyada.jdollarx
   :noindex:

.. java:type:: public final class NPath

Fields
------
n
^

.. java:field:: public final int n
   :outertype: NPath

path
^^^^

.. java:field:: public final Path path
   :outertype: NPath

qualifier
^^^^^^^^^

.. java:field:: public final RelationOperator qualifier
   :outertype: NPath

Constructors
------------
NPath
^^^^^

.. java:constructor:: public NPath(Path path, int n, RelationOperator qualifier)
   :outertype: NPath

Methods
-------
atLeast
^^^^^^^

.. java:method:: public static NPathBuilder atLeast(int n)
   :outertype: NPath

atMost
^^^^^^

.. java:method:: public static NPathBuilder atMost(int n)
   :outertype: NPath

exactly
^^^^^^^

.. java:method:: public static NPathBuilder exactly(int n)
   :outertype: NPath

