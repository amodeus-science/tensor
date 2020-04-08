// code by cr and jph
package ch.ethz.idsc.tensor.pdf;

import java.io.Serializable;
import java.util.Random;
import java.util.stream.DoubleStream;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;

/** fallback option to robustly generate random variates from a
 * {@link BinomialDistribution} for any parameters n and p.
 * The complexity of a single random variate generation is O(n).
 * 
 * <p>For large n, and p away from 0, or 1, the option to approximate the
 * distribution as a {@link NormalDistribution} should be considered.
 * 
 * @see BinomialDistribution */
// implementation by Claudio Ruch
/* package */ class BinomialRandomVariate implements Distribution, //
    MeanInterface, RandomVariateInterface, VarianceInterface, Serializable {
  private final int n;
  private final Scalar p;

  public BinomialRandomVariate(int n, Scalar p) {
    this.n = n;
    this.p = p;
  }

  @Override // from RandomVariateInterface
  public Scalar randomVariate(Random random) {
    double p_success = p.number().doubleValue();
    return RealScalar.of(DoubleStream.generate(random::nextDouble) //
        .limit(n) //
        .filter(value -> value < p_success) //
        .count());
  }

  @Override // from MeanInterface
  public Scalar mean() {
    return RealScalar.of(n).multiply(p);
  }

  @Override // from VarianceInterface
  public Scalar variance() {
    return mean().multiply(RealScalar.ONE.subtract(p));
  }
}
