Geomatrix
=========

This project is about exploring a different approach to represent bidimensional matrices of booleans using geometric properties.
The key concept is to visualize matrices as areas. For example, the following matrix can be seen as an 'L' shaped areas:

<pre>
  000111000000000
  000111000000000
  000111000000000
  000111111110000
  000111111110000
  000000000000000
</pre>

Geomatrix represents matrixes as the set of its vertices. For example, the previous area can be represented by the set {(0,3), (0,6), (3,6), (3,11), (5,6), (5,11)}. In general, *any matrix can be represented unequivocally by a set of vertices*.


The goal of Geomatrix is to provide:
- Space efficient representation of matrixes which show certain "geometric properties"
- Support the following operations: check a random element (check if a position is a 1), chech whether a matrix is contained in another, set operations such as union, intersection and difference of areas, iterate through the contained elements and more.

**Fundamental requirement: the asymtotic cost of all operations must depend only on the number of vertices of the areas.** In particular, it must never depend on the number of elements.


Potential applications:
- Represent matrixes which model a physical space which contain mostly rectangular objects (like a room, or a city).

Geomatrix was used in *Interior Design* (https://github.com/parsons-project/interiores), an application to design rooms and distribute furniture automatically.

Development
-----------
This is a 3 phase project.

1. **Implementation:**
  - Formal definition of the operations supported by Geomatrix
  - Develop, analyze and implement efficient algorithms for those operations.
  - Make a little applet to visualize and test the operations.

2. **Empirical Efficiency tests:** *(i.e. is Geomatrix actually useful at all?)*
  - Make alternative, more conventional implementations of other data structures that support the same operations.
  - Define formal criterions to determine how "appropiate" a matrix is to be represented with a Geomatrix.
  - Carry out efficiency tests based on these criteria for the Geomatrix and other data structures, to get a notion of the usefulness of Geomatrixs *(i.e. how "well-behaved" must be the matrix in order to be faster with Geomatrix?)*

3. **Extension:**
  - Extend the concept to matrixes of integers. The idea is that a matrix of integers can be represented as a set of flat areas at different heights, where the heights are the values.
  - Extend the concept to boolean matrixes of an arbitrary number of dimensions. For example, 3-dimensional matrixes can be represented by a set of vertices which define 3-dimensional figures.

Current development point: finishing phase 1/starting phase 2.



Contact info:
Nil Mamano (tehotserver@gmail.com)
