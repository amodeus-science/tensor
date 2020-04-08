// code by jph
package ch.ethz.idsc.tensor.pdf;

import java.util.Random;

import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.IntegerQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;

/** functionality and suggested base class for a discrete probability distribution */
public abstract class AbstractDiscreteDistribution implements DiscreteDistribution, //
    InverseCDF, MeanInterface {
  @Override // from RandomVariateInterface
  public final Scalar randomVariate(Random random) {
    return protected_quantile(DoubleScalar.of(random.nextDouble()));
  }

  @Override // from PDF
  public final Scalar at(Scalar x) {
    return IntegerQ.of(x) //
        ? p_equals(Scalars.intValueExact(x))
        : RealScalar.ZERO;
  }

  @Override // from DiscreteDistribution
  public final Scalar p_equals(int n) {
    return lowerBound() <= n //
        ? protected_p_equals(n)
        : RealScalar.ZERO;
  }

  /** @param p in the semi-open interval [0, 1)
   * @return */
  protected abstract Scalar protected_quantile(Scalar p);

  /** @param n with lowerBound() <= n
   * @return P(X == n), i.e. probability of random variable X == n */
  protected abstract Scalar protected_p_equals(int n);
}
