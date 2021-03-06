/*
 * Copyright 2001-2013 Artima, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalactic

import scala.collection.{GenMap, mutable, GenTraversableOnce}
import scala.collection.immutable.TreeSet
import scala.collection.immutable.SortedSet
import scala.language.higherKinds

class EquaSets[T](val equality: HashingEquality[T]) { thisEquaSets =>

  case class EquaBox(value: T) {
    override def equals(o: Any): Boolean = 
      o match {
        case other: EquaBox => equality.areEqual(value, other.value)
        case _ => false
      }
    override def hashCode: Int = equality.hashCodeFor(value)
    override def toString: String = s"EquaBox(${value.toString})"
  }

  class EquaBridge[S](from: List[S]) {
    def collect(pf: PartialFunction[S, T]): thisEquaSets.EquaSet =
      thisEquaSets.FastEquaSet.empty ++ (from collect pf) // { case s if pf.isDefinedAt(s) => pf(s) })
  }

  trait EquaSet extends Function1[T, Boolean] with Equals {

    /**
     * Creates a new `EquaSet` with an additional element, unless the element is
     * already present.
     *
     * @param elem the element to be added
     * @return a new `EquaSet` that contains all elements of this `EquaSet` and that also
     * contains `elem`.
     */
    def + (elem: T): thisEquaSets.EquaSet

    /**
     * Creates a new `EquaSet` with additional elements.
     *
     * This method takes two or more elements to be added. Another overloaded
     * variant of this method handles the case where a single element is added.
     *
     * @param elem1 the first element to add.
     * @param elem2 the second element to add.
     * @param elems the remaining elements to add.
     * @return a new `EquaSet` with the given elements added.
     */
    def + (elem1: T, elem2: T, elems: T*): thisEquaSets.EquaSet

    /** Creates a new `EquaSet` by adding all elements contained in another collection to this `EquaSet`.
      *
      *  @param elems     the collection containing the added elements.
      *  @return          a new `EquaSet` with the given elements added.
      */
    def ++ (elems: GenTraversableOnce[T]): thisEquaSets.EquaSet

    /**
     * Creates a new `EquaSet` by adding elements contained in another `EquaSet`.
     *
     * @param that     the other `EquaSet` containing the added elements.
     * @return         a new `EquaSet` with the given elements added.
     */
    def ++ (that: thisEquaSets.EquaSet): thisEquaSets.EquaSet

    /**
     * Creates a new `EquaSet` with a given element removed from this `EquaSet`.
     *
     * @param elem the element to be removed
     * @return a new `EquaSet` that contains all elements of this `EquaSet` but that does not
     * contain `elem`.
     */
    def - (elem: T): thisEquaSets.EquaSet

    /* * USE LATER
     * Creates a new `EquaSet` from this `EquaSet` by removing all elements of another
     * collection.
     *
     * @param xs the collection containing the removed elements.
     * @return a new `EquaSet` that contains all elements of the current `EquaSet`
     * except one less occurrence of each of the elements of `elems`.
     */

    /**
     * Creates a new `EquaSet` from this `EquaSet` with some elements removed.
     *
     * This method takes two or more elements to be removed. Another overloaded
     * variant of this method handles the case where a single element is
     * removed.
     * @param elem1 the first element to remove.
     * @param elem2 the second element to remove.
     * @param elems the remaining elements to remove.
     * @return a new `EquaSet` that contains all elements of the current `EquaSet`
     * except one less occurrence of each of the given elements.
     */
    def - (elem1: T, elem2: T, elems: T*): thisEquaSets.EquaSet

    /**
     * Creates a new `EquaSet` from this `EquaSet` by removing all elements of another
     *  collection.
     *
     *  @param elems     the collection containing the removed elements.
     *  @return a new `EquaSet` that contains all elements of the current `EquaSet`
     *  except one less occurrence of each of the elements of `elems`.
     */
    def --(elems: GenTraversableOnce[T]): thisEquaSets.EquaSet

    /**
     * Creates a new `EquaSet` from this `EquaSet` by removing all elements of another `EquaSet`
     *
     * @param that       the other `EquaSet` containing the removed elements.
     * @return a new `EquaSet` that contains all elements of the current `EquaSet` minus elements contained in the passed in `EquaSet`.
     */
    def --(that: thisEquaSets.EquaSet): thisEquaSets.EquaSet

    /**
     * Applies a binary operator to a start value and all elements of this `EquaSet`,
     *  going left to right.
     *
     *  Note: `/:` is alternate syntax for `foldLeft`; `z /: xs` is the same as
     *  `xs foldLeft z`.
     *
     *  Examples:
     *
     *  Note that the folding function used to compute b is equivalent to that used to compute c.
     *  {{{
     *      scala> val a = List(1,2,3,4)
     *      a: List[Int] = List(1, 2, 3, 4)
     *
     *      scala> val b = (5 /: a)(_+_)
     *      b: Int = 15
     *
     *      scala> val c = (5 /: a)((x,y) => x + y)
     *      c: Int = 15
     *  }}}
     *
     *  $willNotTerminateInf
     *  $orderDependentFold
     *
     *  @param   z    the start value.
     *  @param   op   the binary operator.
     *  @tparam  B    the result type of the binary operator.
     *  @return  the result of inserting `op` between consecutive elements of this $coll,
     *           going left to right with the start value `z` on the left:
     *           {{{
     *             op(...op(op(z, x_1), x_2), ..., x_n)
     *           }}}
     *           where `x,,1,,, ..., x,,n,,` are the elements of this $coll.
     */
    def /:[B](z: B)(op: (B, T) => B): B

    /**
     * Applies a binary operator to all elements of this `EquaSet` and a start value,
     *  going right to left.
     *
     *  Note: `:\` is alternate syntax for `foldRight`; `xs :\ z` is the same as
     *  `xs foldRight z`.
     *  $willNotTerminateInf
     *  $orderDependentFold
     *
     *  Examples:
     *
     *  Note that the folding function used to compute b is equivalent to that used to compute c.
     *  {{{
     *      scala> val a = List(1,2,3,4)
     *      a: List[Int] = List(1, 2, 3, 4)
     *
     *      scala> val b = (a :\ 5)(_+_)
     *      b: Int = 15
     *
     *      scala> val c = (a :\ 5)((x,y) => x + y)
     *      c: Int = 15
     *
     *  }}}
     *
     *  @param   z    the start value
     *  @param   op   the binary operator
     *  @tparam  B    the result type of the binary operator.
     *  @return  the result of inserting `op` between consecutive elements of this $coll,
     *           going right to left with the start value `z` on the right:
     *           {{{
     *             op(x_1, op(x_2, ... op(x_n, z)...))
     *           }}}
     *           where `x,,1,,, ..., x,,n,,` are the elements of this $coll.
     */
    def :\[B](z: B)(op: (T, B) => B): B

    /**
     * Computes the union between this `EquaSet` and another `EquaSet`.
     *
     * '''Note:''' Same as `union`.
     * @param that the `EquaSet` to form the union with.
     * @return a new `EquaSet` consisting of all elements that are in this
     * `EquaSet` or in the given `EquaSet` `that`.
     */
    def | (that: thisEquaSets.EquaSet): thisEquaSets.EquaSet

    /**
     * Computes the intersection between this `EquaSet` and another `EquaSet`.
     *
     * '''Note:''' Same as `intersect`.
     * @param that the `EquaSet` to intersect with.
     * @return a new `EquaSet` consisting of all elements that are both in this
     * `EquaSet` and in the given `EquaSet` `that`.
     */
    def & (that: thisEquaSets.EquaSet): thisEquaSets.EquaSet

    /**
     * The difference of this `EquaSet` and another `EquaSet`.
     *
     * '''Note:''' Same as `diff`.
     * @param that the `EquaSet` of elements to exclude.
     * @return a `EquaSet` containing those elements of this
     * `EquaSet` that are not also contained in the given `EquaSet` `that`.
     */
    def &~ (that: thisEquaSets.EquaSet): thisEquaSets.EquaSet

    /**
     * Appends all elements of this `EquaSet` to a string builder.
     *  The written text consists of the string representations (w.r.t. the method
     * `toString`) of all elements of this `EquaSet` without any separator string.
     *
     * Example:
     *
     * {{{
     *      scala> val a = List(1,2,3,4)
     *      a: List[Int] = List(1, 2, 3, 4)
     *
     *      scala> val b = new StringBuilder()
     *      b: StringBuilder =
     *
     *      scala> val h = a.addString(b)
     *      h: StringBuilder = 1234
     * }}}
     *
     *  @param  b    the string builder to which elements are appended.
     *  @return      the string builder `b` to which elements were appended.
     */
    def addString(b: StringBuilder): StringBuilder

    /**
     * Appends all elements of this `EquaSet` to a string builder using a separator string.
     *  The written text consists of the string representations (w.r.t. the method `toString`)
     *  of all elements of this $coll, separated by the string `sep`.
     *
     * Example:
     *
     * {{{
     *      scala> val a = List(1,2,3,4)
     *      a: List[Int] = List(1, 2, 3, 4)
     *
     *      scala> val b = new StringBuilder()
     *      b: StringBuilder =
     *
     *      scala> a.addString(b, ", ")
     *      res0: StringBuilder = 1, 2, 3, 4
     * }}}
     *
     *  @param  b    the string builder to which elements are appended.
     *  @param sep   the separator string.
     *  @return      the string builder `b` to which elements were appended.
     */
    def addString(b: StringBuilder, sep: String): StringBuilder

    /** Appends all elements of this $coll to a string builder using start, end, and separator strings.
     *  The written text begins with the string `start` and ends with the string `end`.
     *  Inside, the string representations (w.r.t. the method `toString`)
     *  of all elements of this `EquaSet` are separated by the string `sep`.
     *
     * Example:
     *
     * {{{
     *      scala> val a = List(1,2,3,4)
     *      a: List[Int] = List(1, 2, 3, 4)
     *
     *      scala> val b = new StringBuilder()
     *      b: StringBuilder =
     *
     *      scala> a.addString(b , "List(" , ", " , ")")
     *      res5: StringBuilder = List(1, 2, 3, 4)
     * }}}
     *
     *  @param  b    the string builder to which elements are appended.
     *  @param start the starting string.
     *  @param sep   the separator string.
     *  @param end   the ending string.
     *  @return      the string builder `b` to which elements were appended.
     */
    def addString(b: StringBuilder, start: String, sep: String, end: String): StringBuilder

    /**
     * Aggregates the results of applying an operator to subsequent elements.
     *
     *  This is a more general form of `fold` and `reduce`. It has similar
     *  semantics, but does not require the result to be a supertype of the
     *  element type. It traverses the elements in different partitions
     *  sequentially, using `seqop` to update the result, and then applies
     *  `combop` to results from different partitions. The implementation of
     *  this operation may operate on an arbitrary number of collection
     *  partitions, so `combop` may be invoked an arbitrary number of times.
     *
     *  For example, one might want to process some elements and then produce
     *  a `Set`. In this case, `seqop` would process an element and append it
     *  to the list, while `combop` would concatenate two lists from different
     *  partitions together. The initial value `z` would be an empty set.
     *  {{{
     *    pc.aggregate(Set[Int]())(_ += process(_), _ ++ _)
     *  }}}
     *
     *  Another example is calculating geometric mean from a collection of doubles
     *  (one would typically require big doubles for this).
     *
     *  @tparam B        the type of accumulated results
     *  @param z         the initial value for the accumulated result of the partition - this
     *                   will typically be the neutral element for the `seqop` operator (e.g.
     *                   `Nil` for list concatenation or `0` for summation) and may be evaluated
     *                   more than once
     *  @param seqop     an operator used to accumulate results within a partition
     *  @param combop    an associative operator used to combine results from different partitions
     */
    def aggregate[B](z: =>B)(seqop: (B, T) => B, combop: (B, B) => B): B

    /**
     * Tests if some element is contained in this set.
     *
     *  This method is equivalent to `contains`. It allows sets to be interpreted as predicates.
     *  @param elem the element to test for membership.
     *  @return  `true` if `elem` is contained in this set, `false` otherwise.
     */
    def apply(elem: T): Boolean

    /**
     * Builds a new collection by applying a partial function to all elements of this `EquaSet`
     * on which the function is defined.
     *
     * @param pf the partial function which filters and maps the `EquaSet`.
     * @return a new collection of type `That` resulting from applying the partial function
     * `pf` to each element on which it is defined and collecting the results.
     * The order of the elements is preserved.
     *
     * @return a new `EquaSet` resulting from applying the given partial function
     * `pf` to each element on which it is defined and collecting the results.
     * The order of the elements is preserved.
     */
    def collect(pf: PartialFunction[T, T]): thisEquaSets.EquaSet

    /**
     * Builds a new collection by applying a partial function to all elements of this `EquaSet`
     * on which the function is defined.
     *
     * @param thatEquaSets the `EquaSets` into which to filter and maps the `EquaSet`.
     * @param pf the partial function which filters and maps the `EquaSet`.
     * @return a new collection of type `That` resulting from applying the partial function
     * `pf` to each element on which it is defined and collecting the results.
     * The order of the elements is preserved.
     *
     * @return a new `EquaSet` resulting from applying the given partial function
     * `pf` to each element on which it is defined and collecting the results.
     * The order of the elements is preserved.
     */
    def collectInto[U](thatEquaSets: EquaSets[U])(pf: PartialFunction[T, U]): thatEquaSets.EquaSet
    def collectInto[U](thatEquaSets: SortedEquaSets[U])(pf: PartialFunction[T, U]): thatEquaSets.SortedEquaSet

    /**
     * Copies values of this `EquaSet` to an array.
     * Fills the given array `xs` with values of this `EquaSet`.
     * Copying will stop once either the end of the current `EquaSet` is reached,
     * or the end of the array is reached.
     *
     * @param xs the array to fill.
     *
     */
    def copyToArray(xs: Array[thisEquaSets.EquaBox]): Unit

    /**
     * Copies values of this `EquaSet` to an array.
     * Fills the given array `xs` with values of this `EquaSet`, beginning at index `start`.
     * Copying will stop once either the end of the current `EquaSet` is reached,
     * or the end of the array is reached.
     *
     * @param xs the array to fill.
     * @param start the starting index.
     *
     */
    def copyToArray(xs: Array[thisEquaSets.EquaBox], start: Int): Unit

    /**
     * Copies values of this `EquaSet` to an array.
     * Fills the given array `xs` with values of this `EquaSet`, beginning at index `start`.
     * Copying will stop once the count of element copied reach <code>len</code>.
     *
     * @param xs the array to fill.
     * @param start the starting index.
     * @param len the length of elements to copy
     *
     */
    def copyToArray(xs: Array[thisEquaSets.EquaBox], start: Int, len: Int): Unit

    /**
     * Copies all elements of this `EquaSet` to a buffer.
     *
     * @param dest The buffer to which elements are copied.
     */
    def copyToBuffer(dest: mutable.Buffer[thisEquaSets.EquaBox]): Unit

    /**
     * Counts the number of elements in the $coll which satisfy a predicate.
     *
     * @param p the predicate used to test elements.
     * @return the number of elements satisfying the predicate `p`.
     */
    def count(p: T => Boolean): Int

    /**
     * Computes the difference of this `EquaSet` and another `EquaSet`.
     *
     * @param that the `EquaSet` of elements to exclude.
     * @return a `EquaSet` containing those elements of this
     * `EquaSet` that are not also contained in the given `EquaSet` `that`.
     */
    def diff(that: thisEquaSets.EquaSet): thisEquaSets.EquaSet

    /**
     * Selects all elements except first ''n'' ones.
     *
     * @param n the number of elements to drop from this `EquaSet`.
     * @return a `EquaSet` consisting of all elements of this `EquaSet` except the first `n` ones, or else the
     * empty `EquaSet`, if this `EquaSet` has less than `n` elements.
     */
    def drop(n: Int): thisEquaSets.EquaSet

    /**
     * Selects all elements except last ''n'' ones.
     *
     * @param n The number of elements to take
     * @return a `EquiSet` consisting of all elements of this `EquiSet` except the last `n` ones, or else the
     * empty `EquiSet`, if this `EquiSet` has less than `n` elements.
     */
    def dropRight(n: Int): thisEquaSets.EquaSet

    /**
     * Drops longest prefix of elements that satisfy a predicate.
     *
     * @param pred The predicate used to test elements.
     * @return the longest suffix of this `EquiSet` whose first element
     * does not satisfy the predicate `p`.
     */
    def dropWhile(pred: T => Boolean): thisEquaSets.EquaSet

    /**
     * Check if this `EquaSet` contains element which satisfy a predicate.
     *
     * @param pred predicate predicate used to test elements
     * @return <code>true</code> if there's at least one element satisfy the given predicate <code>pred</code>
     */
    def exists(pred: T => Boolean): Boolean

    /** Selects all elements of this `EquaSet` which satisfy a predicate.
      *
      * @param pred the predicate used to test elements.
      * @return a new `EquaSet` consisting of all elements of this `EquaSet` that satisfy the given
      * predicate <code>pred</code>. Their order may not be preserved.
      */
    def filter(pred: T => Boolean): thisEquaSets.EquaSet

    /** Selects all elements of this `EquaSets` which do not satisfy a predicate.
      *
      * @param pred the predicate used to test elements.
      * @return a new `EquaSets` consisting of all elements of this `EquaSets` that do not satisfy the given
      * predicate <code>pred</code>. Their order may not be preserved.
      */
    def filterNot(pred: T => Boolean): thisEquaSets.EquaSet

    /**
     * Finds the first element of the `EquaSet` satisfying a predicate, if any.
     *
     *
     * @param pred the predicate used to test elements.
     * @return an option value containing the first element in the `EquaSet`
     * that satisfies <code>pred</code>, or <code>None</code> if none exists.
     */
    def find(pred: T => Boolean): Option[EquaBox]

    /**
     * Folds the elements of this `EquiSet` using the specified associative
     * binary operator.
     *
     * @tparam T1 a type parameter for the binary operator, a supertype of `A`.
     * @param z a neutral element for the fold operation; may be added to the result
     * an arbitrary number of times, and must not change the result (e.g., `Nil` for list concatenation,
     * 0 for addition, or 1 for multiplication.)
     * @param op a binary operator that must be associative
     * @return the result of applying fold operator `op` between all the elements and `z`
     */
    def fold[T1 >: T](z: T1)(op: (T1, T1) => T1): T1

    /**
     * Applies a binary operator to a start value and all elements of this `EquaSet`,
     * going left to right.
     *
     * @param z the start value.
     * @param op the binary operator.
     * @tparam B the result type of the binary operator.
     * @return the result of inserting `op` between consecutive elements of this `EquaSet`,
     * going left to right with the start value `z` on the left:
     * {{{
     * op(...op(z, x_1), x_2, ..., x_n)
     * }}}
     * where `x,,1,,, ..., x,,n,,` are the elements of this `EquaSet`.
     */
    def foldLeft[B](z: B)(op: (B, T) => B): B

    /**
     * Applies a binary operator to all elements of this `EquaSet` and a start value,
     * going right to left.
     *
     * @param z the start value.
     * @param op the binary operator.
     * @tparam B the result type of the binary operator.
     * @return the result of inserting `op` between consecutive elements of this `EquaSet`,
     * going right to left with the start value `z` on the right:
     * {{{
     * op(x_1, op(x_2, ... op(x_n, z)...))
     * }}}
     * where `x,,1,,, ..., x,,n,,` are the elements of this `EquaSet`.
     */
    def foldRight[B](z: B)(op: (T, B) => B): B

    /**
     * Check if all elements in this `EquaSet` satisfy the predicate.
     *
     * @param pred the predicate to check for
     * @return <code>true</code> if all elements satisfy the predicate, <code>false</code> otherwise.
     */
    def forall(pred: T => Boolean): Boolean

    def foreach[U](f: T => U): Unit

    /**
     * Partitions this $coll into a map of `EquaSet`s according to some discriminator function.
     *
     * Note: this method is not re-implemented by views. This means
     * when applied to a view it will always force the view and
     * return a new `EquaSet`.
     *
     * @param f the discriminator function.
     * @tparam K the type of keys returned by the discriminator function.
     * @return A map from keys to `EquaSet`s such that the following invariant holds:
     * {{{
     * (xs groupBy f)(k) = xs filter (x => f(x) == k)
     * }}}
     * That is, every key `k` is bound to a `EquaSet` of those elements `x`
     * for which `f(x)` equals `k`.
     *
     */
    def groupBy[K](f: T => K): GenMap[K, thisEquaSets.EquaSet]

    /**
     * Partitions elements in fixed size `EquaSet`s.
     * @see [[scala.collection.Iterator]], method `grouped`
     *
     * @param size the number of elements per group
     * @return An iterator producing `EquaSet`s of size `size`, except the
     * last will be less than size `size` if the elements don't divide evenly.
     */
    def grouped(size: Int): Iterator[thisEquaSets.EquaSet]

    def hasDefiniteSize: Boolean

    /** Selects the first element of this `EquaSet`.
      *
      * @return the first element of this `EquaSet`.
      * @throws `NoSuchElementException` if the `EquaSet` is empty.
      */
    def head: T

    /** Optionally selects the first element.
      *
      * @return the first element of this `EquaSet` if it is nonempty,
      * `None` if it is empty.
      */
    def headOption: Option[T]

    /**
     * Selects all elements except the last.
     *
     * @return a `EquaSet` consisting of all elements of this `EquaSet`
     * except the last one.
     * @throws `UnsupportedOperationException` if the `EquaSet` is empty.
     */
    def init: thisEquaSets.EquaSet

    /**
     * Iterates over the inits of this `EquaSet`. The first value will be this
     * `EquaSet` and the final one will be an empty `EquaSet`, with the intervening
     * values the results of successive applications of `init`.
     *
     * @return an iterator over all the inits of this `EquaSet`
     * @example EquaSet(1,2,3).inits = Iterator(EquaSet(1,2,3), EquaSet(1,2), EquaSet(1), EquaSet())
     */
    def inits: Iterator[thisEquaSets.EquaSet]

    /**
     * Computes the intersection between this `EquaSet` and another `EquaSet`.
     *
     * @param that the `EquaSet` to intersect with.
     * @return a new `EquaSet` consisting of all elements that are both in this
     * `EquaSet` and in the given `EquaSet` `that`.
     */
    def intersect(that: thisEquaSets.EquaSet): thisEquaSets.EquaSet

    def into[U](thatEquaSets: EquaSets[U]): thatEquaSets.EquaBridge[T]
    def into[U](thatEquaSets: SortedEquaSets[U]): thatEquaSets.EquaBridge[T]

    def isEmpty: Boolean

    /** Tests whether this `EquaSet` can be repeatedly traversed. Always
      * true for Traversables and false for Iterators unless overridden.
      *
      * @return `true` if it is repeatedly traversable, `false` otherwise.
      */
    def isTraversableAgain: Boolean

    def iterator: Iterator[T]

    /**
     * Selects the last element.
     *
     * @return The last element of this `EquaSet`.
     * @throws NoSuchElementException If the `EquaSet` is empty.
     */
    def last: T

    /**
     * Optionally selects the last element.
     *
     * @return the last element of this `EquaSet` if it is nonempty,
     * `None` if it is empty.
     */
    def lastOption: Option[T]

    /**
     * Finds the largest element.
      *
      * @param ord An ordering to be used for comparing elements.
      * @tparam T1 The type over which the ordering is defined.
      * @return the largest element of this `EquaSet` with respect to the ordering `ord`.
      *
      * @return the largest element of this `EquaSet`.
      */
    def max[T1 >: T](implicit ord: Ordering[T1]): T

    /**
     * Finds the first element which yields the largest value measured by function f.
     *
     * @param cmp An ordering to be used for comparing elements.
     * @tparam B The result type of the function f.
     * @param f The measuring function.
     * @return the first element of this `EquaSet` with the largest value measured by function f
     * with respect to the ordering `cmp`.
     *
     * @return the first element of this `EquaSet` with the largest value measured by function f.
     */
    def maxBy[B](f: T => B)(implicit cmp: Ordering[B]): T

    /**
     * Finds the smallest element.
     *
     * @param ord An ordering to be used for comparing elements.
     * @tparam T1 The type over which the ordering is defined.
     * @return the smallest element of this `EquaSet` with respect to the ordering `ord`.
     *
     * @return the smallest element of this `EquaSet`
     */
    def min[T1 >: T](implicit ord: Ordering[T1]): T

    /**
     * Finds the first element which yields the smallest value measured by function f.
     *
     * @param cmp An ordering to be used for comparing elements.
     * @tparam B The result type of the function f.
     * @param f The measuring function.
     * @return the first element of this `EquaSet` with the smallest value measured by function f
     * with respect to the ordering `cmp`.
     *
     * @return the first element of this `EquaSet` with the smallest value measured by function f.
     */
    def minBy[B](f: T => B)(implicit cmp: Ordering[B]): T

    /**
     * Displays all elements of this `EquaSet` in a string using start, end, and
     * separator strings.
     *
     * @param start the starting string.
     * @param sep the separator string.
     * @param end the ending string.
     * @return a string representation of this `EquaSet`. The resulting string
     * begins with the string `start` and ends with the string
     * `end`. Inside, the string representations (w.r.t. the method
     * `toString`) of all elements of this $coll are separated by
     * the string `sep`.
     *
     * @example `EquaSet(1, 2, 3).mkString("(", "; ", ")") = "(1; 2; 3)"`
     */
    def mkString(start: String, sep: String, end: String): String
    /**
     * Displays all elements of this `EquaSet` in a string using a separator string.
     *
     * @param sep the separator string.
     * @return a string representation of this `EquaSet`. In the resulting string
     * the string representations (w.r.t. the method `toString`)
     * of all elements of this `EquaSet` are separated by the string `sep`.
     *
     * @example `EquaSet(1, 2, 3).mkString("|") = "1|2|3"`
     */
    def mkString(sep: String): String
    /**
     * Displays all elements of this `EquaSet` in a string.
     *
     * @return a string representation of this `EquaSet`. In the resulting string
     * the string representations (w.r.t. the method `toString`)
     * of all elements of this `EquaSet` follow each other without any
     * separator string.
     */
    def mkString: String

    /** Tests whether the `EquaSet` is not empty.
      *
      * @return `true` if the `EquaSet` contains at least one element, `false` otherwise.
      */
    def nonEmpty: Boolean

    /**
     * Partitions this `EquaSet` in two `EquaSet`s according to a predicate.
     *
     * @param pred the predicate on which to partition.
     * @return a pair of `EquaSet`s: the first `EquaSet` consists of all elements that
     * satisfy the predicate `p` and the second `EquaSet` consists of all elements
     * that don't. The relative order of the elements in the resulting `EquaSet`s
     * may not be preserved.
     */
    def partition(pred: T => Boolean): (thisEquaSets.EquaSet, thisEquaSets.EquaSet)

    /**
     * Multiplies up the elements of this collection.
     *
     * @param num an implicit parameter defining a set of numeric operations
     * which includes the `*` operator to be used in forming the product.
     * @tparam T1 the result type of the `*` operator.
     * @return the product of all elements of this `EquaSet` with respect to the `*` operator in `num`.
     *
     * @return the product of all elements in this `EquaSet` of numbers of type `Int`.
     * Instead of `Int`, any other type `T` with an implicit `Numeric[T]` implementation
     * can be used as element type of the `EquaSet` and as result type of `product`.
     * Examples of such types are: `Long`, `Float`, `Double`, `BigInt`.
     */
    def product[T1 >: T](implicit num: Numeric[T1]): T1

    def size: Int
    def toSet: Set[thisEquaSets.EquaBox]
    def union(that: thisEquaSets.EquaSet): thisEquaSets.EquaSet

    private[scalactic] def owner: EquaSets[T] = thisEquaSets
  }

  class FastEquaBridge[S](from: List[S]) extends EquaBridge[S](from) {
    override def collect(pf: PartialFunction[S, T]): thisEquaSets.FastEquaSet =
      thisEquaSets.FastEquaSet.empty ++ (from collect pf)
  }

  class FastEquaSet private[scalactic] (private val underlying: Set[EquaBox]) extends EquaSet {
    def + (elem: T): thisEquaSets.FastEquaSet = new FastEquaSet(underlying + EquaBox(elem))
    def + (elem1: T, elem2: T, elem3: T*): thisEquaSets.FastEquaSet =
      new FastEquaSet(underlying + (EquaBox(elem1), EquaBox(elem2), elem3.map(EquaBox(_)): _*))
    def ++ (elems: GenTraversableOnce[T]): thisEquaSets.FastEquaSet =
      new FastEquaSet(underlying ++ elems.toSeq.map(EquaBox(_)))
    def ++ (that: thisEquaSets.EquaSet): thisEquaSets.FastEquaSet = new FastEquaSet(underlying ++ that.toSet)
    def - (elem: T): thisEquaSets.FastEquaSet = new FastEquaSet(underlying - EquaBox(elem))
    def - (elem1: T, elem2: T, elem3: T*): thisEquaSets.FastEquaSet =
      new FastEquaSet(underlying - (EquaBox(elem1), EquaBox(elem2), elem3.map(EquaBox(_)): _*))
    def --(elems: GenTraversableOnce[T]): thisEquaSets.EquaSet =
      new FastEquaSet(underlying -- elems.toSeq.map(EquaBox(_)))
    def --(that: thisEquaSets.EquaSet): thisEquaSets.EquaSet =
      new FastEquaSet(underlying -- that.toSet)
    def /:[B](z: B)(op: (B, T) => B): B =
      underlying./:(z)((b: B, e: EquaBox) => op(b, e.value))
    def :\[B](z: B)(op: (T, B) => B): B =
      underlying.:\(z)((e: EquaBox, b: B) => op(e.value, b))
    def | (that: thisEquaSets.EquaSet): thisEquaSets.FastEquaSet = this union that
    def & (that: thisEquaSets.EquaSet): thisEquaSets.FastEquaSet = this intersect that
    def &~ (that: thisEquaSets.EquaSet): thisEquaSets.FastEquaSet = this diff that
    def addString(b: StringBuilder): StringBuilder = underlying.map(_.value).addString(b)
    def addString(b: StringBuilder, sep: String): StringBuilder = underlying.map(_.value).addString(b, sep)
    def addString(b: StringBuilder, start: String, sep: String, end: String): StringBuilder = underlying.map(_.value).addString(b, start, sep, end)
    def aggregate[B](z: =>B)(seqop: (B, T) => B, combop: (B, B) => B): B = underlying.aggregate(z)((b: B, e: EquaBox) => seqop(b, e.value), combop)
    def apply(elem: T): Boolean = underlying.apply(EquaBox(elem))
    def canEqual(that: Any): Boolean = that.isInstanceOf[thisEquaSets.EquaSet] && equality == that.asInstanceOf[thisEquaSets.EquaSet].owner.equality
    def collect(pf: PartialFunction[T, T]): thisEquaSets.EquaSet =
      new FastEquaSet(underlying collect { case hb: thisEquaSets.EquaBox if pf.isDefinedAt(hb.value) => EquaBox(pf(hb.value)) })
    def collectInto[U](thatEquaSets: EquaSets[U])(pf: PartialFunction[T, U]): thatEquaSets.EquaSet =
      new thatEquaSets.FastEquaSet(underlying collect { case hb: thisEquaSets.EquaBox if pf.isDefinedAt(hb.value) => thatEquaSets.EquaBox(pf(hb.value)) })
    def collectInto[U](thatEquaSets: SortedEquaSets[U])(pf: PartialFunction[T, U]): thatEquaSets.SortedEquaSet =
      new thatEquaSets.TreeEquaSet(TreeSet.empty(thatEquaSets.ordering) ++ (underlying collect { case hb: thisEquaSets.EquaBox if pf.isDefinedAt(hb.value) => thatEquaSets.EquaBox(pf(hb.value)) }))
    def copyToArray(xs: Array[thisEquaSets.EquaBox]): Unit = underlying.copyToArray(xs)
    def copyToArray(xs: Array[thisEquaSets.EquaBox], start: Int): Unit = underlying.copyToArray(xs, start)
    def copyToArray(xs: Array[thisEquaSets.EquaBox], start: Int, len: Int): Unit = underlying.copyToArray(xs, start, len)
    def copyToBuffer(dest: mutable.Buffer[thisEquaSets.EquaBox]): Unit = underlying.copyToBuffer(dest)
    def count(p: T => Boolean): Int = underlying.map(_.value).count(p)
    def diff(that: thisEquaSets.EquaSet): thisEquaSets.FastEquaSet =
      new FastEquaSet(underlying diff that.toSet.map((eb: EquaBox) => EquaBox(eb.value)))
    def drop(n: Int): thisEquaSets.EquaSet = new FastEquaSet(underlying.drop(n))
    def dropRight(n: Int): thisEquaSets.EquaSet = new FastEquaSet(underlying.dropRight(n))
    def dropWhile(pred: T => Boolean): thisEquaSets.EquaSet = new FastEquaSet(underlying.dropWhile((p: EquaBox) => pred(p.value)))
    override def equals(other: Any): Boolean =
      other match {
        case equiSet: thisEquaSets.FastEquaSet => 
          underlying == equiSet.underlying
        case _ => false
      }
    def exists(pred: T => Boolean): Boolean = underlying.exists((box: EquaBox) => pred(box.value))
    def filter(pred: T => Boolean): thisEquaSets.EquaSet = new FastEquaSet(underlying.filter((box: EquaBox) => pred(box.value)))
    def filterNot(pred: T => Boolean): thisEquaSets.EquaSet = new FastEquaSet(underlying.filterNot((box: EquaBox) => pred(box.value)))
    def find(pred: T => Boolean): Option[EquaBox] = underlying.find((box: EquaBox) => pred(box.value))
    def fold[T1 >: T](z: T1)(op: (T1, T1) => T1): T1 = underlying.map(_.value).fold[T1](z)(op)
    def foldLeft[B](z: B)(op: (B, T) => B): B = underlying.map(_.value).foldLeft[B](z)(op)
    def foldRight[B](z: B)(op: (T, B) => B): B = underlying.map(_.value).foldRight[B](z)(op)
    def forall(pred: T => Boolean): Boolean = underlying.map(_.value).forall(pred)
    def foreach[U](f: T => U): Unit = underlying.map(_.value).foreach(f)
    def groupBy[K](f: T => K): GenMap[K, thisEquaSets.EquaSet] = underlying.groupBy((box: EquaBox) => f(box.value)).map(t => (t._1, new FastEquaSet(t._2)))
    def grouped(size: Int): Iterator[thisEquaSets.EquaSet] = underlying.grouped(size).map(new FastEquaSet(_))
    def hasDefiniteSize: Boolean = underlying.hasDefiniteSize
    override def hashCode: Int = underlying.hashCode
    def head: T = underlying.head.value
    def headOption: Option[T] =
      underlying.headOption match {
        case Some(head) => Some(head.value)
        case None => None
      }
    def init: thisEquaSets.EquaSet = new FastEquaSet(underlying.init)
    def inits: Iterator[thisEquaSets.EquaSet] = underlying.inits.map(new FastEquaSet(_))
    def intersect(that: thisEquaSets.EquaSet): thisEquaSets.FastEquaSet =
      new FastEquaSet(underlying intersect that.toSet.map((eb: EquaBox) => EquaBox(eb.value)))
    def into[U](thatEquaSets: EquaSets[U]): thatEquaSets.FastEquaBridge[T] = new thatEquaSets.FastEquaBridge[T](underlying.toList.map(_.value))
    def into[U](thatEquaSets: SortedEquaSets[U]): thatEquaSets.FastEquaBridge[T] = new thatEquaSets.FastEquaBridge[T](underlying.toList.map(_.value))
    def isEmpty: Boolean = underlying.isEmpty
    def isTraversableAgain: Boolean = underlying.isTraversableAgain
    def iterator: Iterator[T] = underlying.iterator.map(_.value)
    def last: T = underlying.last.value
    def lastOption: Option[T] =
      underlying.lastOption match {
        case Some(last) => Some(last.value)
        case None => None
      }
    def max[T1 >: T](implicit ord: Ordering[T1]): T = underlying.map(_.value).max(ord)
    def maxBy[B](f: T => B)(implicit cmp: Ordering[B]): T = underlying.map(_.value).maxBy(f)
    def min[T1 >: T](implicit ord: Ordering[T1]): T = underlying.map(_.value).min(ord)
    def minBy[B](f: T => B)(implicit cmp: Ordering[B]): T = underlying.map(_.value).minBy(f)
    def mkString(start: String, sep: String, end: String): String = underlying.map(_.value).mkString(start, sep, end)
    def mkString(sep: String): String = underlying.map(_.value).mkString(sep)
    def mkString: String = underlying.map(_.value).mkString
    def nonEmpty: Boolean = underlying.nonEmpty
    def partition(pred: T => Boolean): (thisEquaSets.EquaSet, thisEquaSets.EquaSet) = {
      val tuple2 = underlying.partition((box: EquaBox) => pred(box.value))
      (new FastEquaSet(tuple2._1), new FastEquaSet(tuple2._2))
    }
    def product[T1 >: T](implicit num: Numeric[T1]): T1 = underlying.map(_.value).product(num)
    def size: Int = underlying.size
    def toSet: Set[thisEquaSets.EquaBox] = underlying
    // Be consistent with standard library. HashSet's toString is Set(1, 2, 3)
    override def toString: String = s"EquaSet(${underlying.toVector.map(_.value).mkString(", ")})"
    def union(that: thisEquaSets.EquaSet): thisEquaSets.FastEquaSet =
      new FastEquaSet(underlying union that.toSet.map((eb: EquaBox) => EquaBox(eb.value)))
  }
  object FastEquaSet {
    def empty: FastEquaSet = new FastEquaSet(Set.empty)
    def apply(elems: T*): FastEquaSet = 
      new FastEquaSet(Set(elems.map(EquaBox(_)): _*))
  }
  object EquaSet {
    def empty: EquaSet = FastEquaSet.empty
    def apply(elems: T*): EquaSet = FastEquaSet(elems: _*)
  }
}

object EquaSets {
  def apply[T](equality: HashingEquality[T]): EquaSets[T] = new EquaSets(equality)
}
