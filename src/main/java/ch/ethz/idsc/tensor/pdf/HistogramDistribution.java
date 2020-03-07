// code by jph and gjoel
package ch.ethz.idsc.tensor.pdf;

import java.io.Serializable;
import java.util.Random;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.LinearInterpolation;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Min;
import ch.ethz.idsc.tensor.sca.AbsSquared;
import ch.ethz.idsc.tensor.sca.Clips;
import ch.ethz.idsc.tensor.sca.Floor;
import ch.ethz.idsc.tensor.sca.Increment;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** A histogram distribution approximates an unknown continuous distribution using
 * a collection of observed samples from the distribution.
 * 
 * <p>The current implementation is characterized by the following properties
 * <ul>
 * <li>the probability density for value x in of histogram distribution is a piecewise constant function, and
 * <li>the user-specified, constant width of each bin.
 * </ul>
 * 
 * <p>The implementation combines
 * {@link EmpiricalDistribution}, {@link BinCounts}, and {@link UniformDistribution#unit()}.
 * 
 * <p>Other approximation methods may be implemented in the future.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/HistogramDistribution.html">HistogramDistribution</a> */
public class HistogramDistribution implements //
    ContinuousDistribution, InverseCDF, MeanInterface, VarianceInterface, Serializable {
  /** Example:
   * HistogramDistribution[{10.2, -1.6, 3.2, -0.4, 11.5, 7.3, 3.8, 9.8}, 2]
   * 
   * <p>The implementation also supports input of type {@link Quantity}.
   * 
   * @param samples vector
   * @param width of bins over which to assume uniform distribution, i.e. constant PDF
   * @return */
  public static Distribution of(Tensor samples, Scalar width) {
    return new HistogramDistribution(samples, width);
  }

  /** @param samples
   * @param binningMethod
   * @return histogram distribution with bin width computed from given binning method */
  public static Distribution of(Tensor samples, BinningMethod binningMethod) {
    return of(samples, binningMethod.apply(samples));
  }

  /** @param samples
   * @return histogram distribution with bin width computed from freedman-diaconis rule */
  public static Distribution of(Tensor samples) {
    return of(samples, BinningMethod.IQR);
  }

  /***************************************************/
  private final ScalarUnaryOperator discrete;
  private final ScalarUnaryOperator original;
  private final EmpiricalDistribution empiricalDistribution;
  private final Scalar width;
  private final Scalar width_half;

  private HistogramDistribution(Tensor samples, Scalar width) {
    Scalar min = Floor.toMultipleOf(width).apply(samples.stream().reduce(Min::of).get().Get());
    discrete = scalar -> scalar.subtract(min).divide(width);
    original = scalar -> scalar.multiply(width).add(min);
    empiricalDistribution = //
        (EmpiricalDistribution) EmpiricalDistribution.fromUnscaledPDF(BinCounts.of(samples.map(discrete)));
    this.width = width;
    width_half = width.multiply(RationalScalar.HALF);
  }

  @Override // from PDF
  public Scalar at(Scalar x) {
    return empiricalDistribution.at(Floor.FUNCTION.apply(discrete.apply(x)));
  }

  @Override // from MeanInterface
  public Scalar mean() {
    return original.apply(empiricalDistribution.mean()).add(width_half);
  }

  @Override // from CDF
  public Scalar p_lessThan(Scalar x) {
    Scalar xlo = discrete.apply(Floor.toMultipleOf(width).apply(x));
    Scalar ofs = Clips.interval(xlo, Increment.ONE.apply(xlo)).rescale(discrete.apply(x));
    return LinearInterpolation.of(Tensors.of( //
        empiricalDistribution.p_lessThan(xlo), //
        empiricalDistribution.p_lessEquals(xlo))).At(ofs);
  }

  @Override // from CDF
  public Scalar p_lessEquals(Scalar x) {
    return p_lessThan(x);
  }

  @Override // from InverseCDF
  public Scalar quantile(Scalar p) {
    Scalar x_floor = empiricalDistribution.quantile(p);
    return original.apply(x_floor.add(Clips.interval( //
        empiricalDistribution.p_lessThan(x_floor), //
        empiricalDistribution.p_lessEquals(x_floor)).rescale(p)));
  }

  @Override // from RandomVariateInterface
  public Scalar randomVariate(Random random) {
    return original.apply(empiricalDistribution.randomVariate(random) //
        .add(RandomVariate.of(UniformDistribution.unit(), random)));
  }

  @Override // from VarianceInterface
  public Scalar variance() {
    return Expectation.variance(empiricalDistribution).add(RationalScalar.of(1, 12)).multiply(AbsSquared.of(width));
  }
}
