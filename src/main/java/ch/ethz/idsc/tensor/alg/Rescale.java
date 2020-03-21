// code by jph
package ch.ethz.idsc.tensor.alg;

import ch.ethz.idsc.tensor.ComplexScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.ScalarQ;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.ScalarSummaryStatistics;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** Rescale so that all the list elements run from 0 to 1
 * 
 * <code>
 * Rescale[{-0.7, 0.5, 1.2, 5.6, 1.8}] == {0., 0.190476, 0.301587, 1., 0.396825}
 * </code>
 * 
 * <p>Mathematica handles Infinity in a non-trivial way.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Rescale.html">Rescale</a> */
public class Rescale {
  /** RealScalar.ZERO is used instead of {@link Scalar#zero()}
   * to eliminate unit of {@link Quantity}. */
  private static final ScalarUnaryOperator FINITE_NUMBER_ZERO = //
      scalar -> isFiniteNumber(scalar) ? RealScalar.ZERO : scalar;

  /** The scalar entries of the given tensor may also be instance of
   * {@link Quantity} with identical unit.
   * 
   * <p>Example:
   * <pre>
   * Rescale[{10, 20, 30}] == {0, 1/2, 1}
   * Rescale[{3[s], Infinity[s], 6[s], 2[s]}] == {1/4, Infinity, 1, 0}
   * </pre>
   * 
   * @param tensor of arbitrary structure
   * @return
   * @throws Exception if any entry is a {@link ComplexScalar} */
  public static Tensor of(Tensor tensor) {
    return new Rescale(tensor).result();
  }

  // helper function
  private static boolean isFiniteNumber(Scalar scalar) {
    return Double.isFinite(scalar.number().doubleValue());
  }

  // helper function
  private static Tensor _result(Tensor tensor, ScalarSummaryStatistics scalarSummaryStatistics) {
    if (0 < scalarSummaryStatistics.getCount()) {
      Scalar min = scalarSummaryStatistics.getMin();
      Scalar max = scalarSummaryStatistics.getMax();
      Scalar width = max.subtract(min);
      if (Scalars.nonZero(width)) // operation is not identical to Clip#rescale
        return tensor.map(scalar -> scalar.subtract(min).divide(width));
    }
    return tensor.map(FINITE_NUMBER_ZERO); // set all finite number entries to 0
  }

  /***************************************************/
  private final ScalarSummaryStatistics scalarSummaryStatistics;
  private final Tensor result;

  /** @param tensor of rank at least 1
   * @throws Exception if given tensor is a scalar */
  public Rescale(Tensor tensor) {
    ScalarQ.thenThrow(tensor);
    scalarSummaryStatistics = tensor.flatten(-1) //
        .map(Scalar.class::cast) //
        .filter(Rescale::isFiniteNumber) //
        .collect(ScalarSummaryStatistics.collector());
    result = _result(tensor, scalarSummaryStatistics);
  }

  public ScalarSummaryStatistics scalarSummaryStatistics() {
    return scalarSummaryStatistics;
  }

  public Tensor result() {
    return result;
  }
}
