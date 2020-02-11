// code by gjoel
package ch.ethz.idsc.tensor.red;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.InverseCDF;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** InterquartileRange of the tensor library only operates on vectors.
 * 
 * Mathematica::InterquartileRange also operates (trivially) on matrices.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/InterquartileRange.html">InterquartileRange</a> */
public enum InterquartileRange {
  ;
  private static final Scalar LO = RationalScalar.of(1, 4);
  private static final Scalar HI = RationalScalar.of(3, 4);

  /** Example:
   * <code>InterquartileRange[{0, 1, 2, 3, 10}] == 2</code>
   * 
   * @param samples unsorted
   * @return interquartile range as scalar */
  public static Scalar of(Tensor samples) {
    ScalarUnaryOperator scalarUnaryOperator = Quantile.of(VectorQ.require(samples));
    return scalarUnaryOperator.apply(HI).subtract(scalarUnaryOperator.apply(LO)).Get();
  }

  /** @param distribution
   * @return interquartile range of given distribution as scalar
   * @throws Exception if given distribution does not implement {@link InverseCDF} */
  public static Scalar of(Distribution distribution) {
    InverseCDF inverseCDF = InverseCDF.of(distribution);
    return inverseCDF.quantile(HI).subtract(inverseCDF.quantile(LO));
  }
}
