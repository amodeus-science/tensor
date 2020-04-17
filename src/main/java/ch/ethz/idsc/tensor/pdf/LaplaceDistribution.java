// code by jph
package ch.ethz.idsc.tensor.pdf;

import java.io.Serializable;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.Log;
import ch.ethz.idsc.tensor.sca.Sign;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/LaplaceDistribution.html">LaplaceDistribution</a> */
public class LaplaceDistribution extends AbstractContinuousDistribution //
    implements InverseCDF, MeanInterface, VarianceInterface, Serializable {
  /** @param mean
   * @param beta positive
   * @return */
  public static Distribution of(Scalar mean, Scalar beta) {
    return new LaplaceDistribution(mean, Sign.requirePositive(beta));
  }

  /** @param mean
   * @param beta positive
   * @return */
  public static Distribution of(Number mean, Number beta) {
    return of(RealScalar.of(mean), RealScalar.of(beta));
  }

  /***************************************************/
  private final Scalar mean;
  private final Scalar beta;

  private LaplaceDistribution(Scalar mean, Scalar beta) {
    this.mean = mean;
    this.beta = beta;
  }

  @Override // from PDF
  public Scalar at(Scalar x) {
    return Exp.FUNCTION.apply(x.subtract(mean).abs().negate().divide(beta)).divide(beta.add(beta));
  }

  @Override // from CDF
  public Scalar p_lessThan(Scalar x) {
    Scalar p = Exp.FUNCTION.apply(x.subtract(mean).abs().negate().divide(beta)).multiply(RationalScalar.HALF);
    return Scalars.lessEquals(mean, x) //
        ? RealScalar.ONE.subtract(p)
        : p;
  }

  @Override // from InverseCDF
  public Scalar quantile(Scalar p) {
    if (Scalars.lessEquals(p, RationalScalar.HALF))
      return mean.add(Log.FUNCTION.apply(p.add(p)).multiply(beta));
    Scalar c = RealScalar.ONE.subtract(p);
    return mean.subtract(Log.FUNCTION.apply(c.add(c)).multiply(beta));
  }

  @Override // from AbstractContinuousDistribution
  protected Scalar randomVariate(double reference) {
    return quantile(RealScalar.of(reference));
  }

  @Override // from MeanInterface
  public Scalar mean() {
    return mean;
  }

  @Override // from VarianceInterface
  public Scalar variance() {
    Scalar b2 = beta.multiply(beta);
    return b2.add(b2);
  }

  @Override // from Object
  public String toString() {
    return String.format("%s[%s, %s]", getClass().getSimpleName(), mean, beta);
  }
}
