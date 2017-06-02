// code by jph
package ch.ethz.idsc.tensor.red;

import java.util.function.BiFunction;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** Hypot computes
 * <code>sqrt(<i>a</i><sup>2</sup>&nbsp;+<i>b</i><sup>2</sup>)</code>
 * for a and b as {@link RealScalar}s
 * without intermediate overflow or underflow.
 * 
 * <p>Hypot also operates on vectors.
 * 
 * <p>Hypot is inspired by {@link Math#hypot(double, double)} */
public enum Hypot implements BiFunction<Scalar, Scalar, Scalar> {
  bifunction;
  // ---
  @Override
  public Scalar apply(Scalar a, Scalar b) {
    // return ofVector(Tensors.of(a,b));
    Scalar ax = a.abs();
    Scalar ay = b.abs();
    Scalar min = Min.of(ax, ay);
    Scalar max = Max.of(ax, ay);
    if (min.equals(RealScalar.ZERO))
      return max; // if min == 0 return max
    // valid at this point: 0 < min <= max
    Scalar ratio = min.divide(max);
    // TODO RealScalar.ONE this is not sufficiently generic
    return max.multiply(Sqrt.function.apply(RealScalar.ONE.add(ratio.multiply(ratio))));
    // Scalar one = min.divide(min);
    // Scalar res = one.add(min.multiply(min));
    // return max.multiply(Sqrt.function.apply(res));
  }

  /** function computes the 2-Norm of a vector
   * without intermediate overflow or underflow
   * 
   * <p>the empty vector evaluates to Hypot[{}] == 0
   * whereas in Mathematica Norm[{}] == Norm[{}]
   * 
   * <p>The disadvantage of the implementation is that
   * a numerical output is returned even in cases where
   * a rational number is the exact result.
   * 
   * @param vector
   * @return */
  public static Scalar ofVector(Tensor vector) {
    if (vector.length() == 0) // <- condition not compliant with Mathematica
      return RealScalar.ZERO;
    Tensor abs = vector.map(Scalar::abs);
    Scalar max = (Scalar) abs.flatten(0).reduce(Max::of).get();
    if (max.equals(max.zero()))
      return max;
    abs = abs.multiply(max.invert());
    return max.multiply(Sqrt.function.apply((Scalar) abs.dot(abs)));
  }
}
