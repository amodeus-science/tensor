// code by jph
package ch.ethz.idsc.tensor.sca.win;

import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** HannWindow[1/2]=0
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/HannWindow.html">HannWindow</a> */
public enum HannWindow implements ScalarUnaryOperator {
  FUNCTION;

  private static final Scalar _1_3 = RationalScalar.of(1, 3);
  private static final Scalar _1_4 = RationalScalar.of(1, 4);
  private static final Scalar _1_6 = RationalScalar.of(1, 6);
  private static final Scalar _3_4 = RationalScalar.of(3, 4);

  @Override
  public Scalar apply(Scalar x) {
    x = x.abs();
    if (Scalars.lessThan(x, RationalScalar.HALF)) {
      if (ExactScalarQ.of(x)) {
        if (x.equals(RealScalar.ZERO))
          return RealScalar.ONE;
        if (x.equals(_1_3))
          return _1_4;
        if (x.equals(_1_4))
          return RationalScalar.HALF;
        if (x.equals(_1_6))
          return _3_4;
      }
      return StaticHelper.deg1(RationalScalar.HALF, RationalScalar.HALF, x);
    }
    return RealScalar.ZERO;
  }

  /** @param tensor
   * @return tensor with all scalars replaced with their function value */
  @SuppressWarnings("unchecked")
  public static <T extends Tensor> T of(T tensor) {
    return (T) tensor.map(FUNCTION);
  }
}
