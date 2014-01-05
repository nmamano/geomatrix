Geomatrix
=========


Geomatrix is about exploring a different approach to represent bidimensional matrices of booleans using geometric properties.
The key concept is to visualize matrices as areas. For example, the following matrix can be seen as an 'L' shaped area:

<pre>
  000111000000000
  000111000000000
  000111000000000
  000111111110000
  000111111110000
  000000000000000
</pre>

Geomatrix represents matrixes as geometric objects. For example, the previous matrix can be represented as an area with vertices {(0,3), (0,6), (3,6), (3,11), (5,11), (5,3)}.

This **[pdf presentation](https://github.com/nmamano/geomatrix/raw/master/geomatrixPresentation.pdf)** explains Geomatrix in detail.

The **[Geomatrix applet](https://github.com/nmamano/geomatrix/raw/master/geomatrix.jar)** lets you visualize and explore the concepts in action *(requires Java Runtime Environment)*.




**Project Goals:**
- Provide a space efficient representation for matrixes which show certain "geometric traits"
- Support a wide spectrum of operations, including set operations, geometric operations and more.
The complexity of most operations is independent of the number of elements. For instance, in Geomatrix it might be equally expensive to set to True one element than one million.

**Potential applications:**
- Represent matrixes which model a physical space which contain mostly rectangular objects (like a room, or a city).

Geomatrix was used in *[Interior Design](https://github.com/parsons-project/interiores)*, an application to design rooms and distribute furniture automatically.

Development
-----------
This is a 3 phase project.

1. **Implementation:**
  - Formal specification of the operations supported by Geomatrix.
  - Develop, analyze and implement efficient algorithms for those operations.
  - Make a little applet to visualize and test the operations.

2. **Empirical Efficiency tests:**
  - Make alternative, more conventional implementations of other data structures that support the same operations.
  - Carry out efficiency tests under different assumptions of the distribution of the data to evaluate the performance of Geomatrix in comparison to the other data structures.

3. **Extension:**
  - Extend the concept to matrixes of integers. The idea is that a matrix of integers can be represented as a set of flat areas at different heights, where the heights are the integer values.
  - Extend the concept to boolean matrixes of an arbitrary number of dimensions. For example, 3-dimensional matrixes can be represented by 3-dimensional figures.

Current development point: finishing phase 1/starting phase 2.
