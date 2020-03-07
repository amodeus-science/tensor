// code by jph
package ch.ethz.idsc.tensor.sca.win;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Cos;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** TukeyWindow[1/2]=0
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/TukeyWindow.html">TukeyWindow</a> */
public enum TukeyWindow implements ScalarUnaryOperator {
  FUNCTION;

  private static final Scalar _1_6 = RationalScalar.of(1, 6);
  private static final Scalar _3_PI = RealScalar.of(3 * Math.PI);

  @Override
  public Scalar apply(Scalar x) {
    x = x.abs();
    if (Scalars.lessEquals(x, _1_6))
      return RealScalar.ONE;
    if (Scalars.lessThan(x, RationalScalar.HALF))
      return RationalScalar.HALF.add(RationalScalar.HALF.multiply(Cos.FUNCTION.apply(x.subtract(_1_6).multiply(_3_PI))));
    return RealScalar.ZERO;
  }

  /** @param tensor
   * @return tensor with all scalars replaced with their function value */
  @SuppressWarnings("unchecked")
  public static <T extends Tensor> T of(T tensor) {
    return (T) tensor.map(FUNCTION);
  }
}
