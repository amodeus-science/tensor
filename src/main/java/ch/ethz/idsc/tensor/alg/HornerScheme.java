// code by jph
package ch.ethz.idsc.tensor.alg;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** Horner's scheme improves speed and stability for the numeric evaluation of large polynomials
 * 
 * <p>Quote from Wikipedia:
 * These methods named after the British mathematician William George Horner,
 * although they were known before him by Paolo Ruffini, six hundred years earlier,
 * by the Chinese mathematician Qin Jiushao and seven hundred years earlier,
 * by the Persian mathematician Sharaf al-Din al-Ṭusi.
 * 
 * <p>https://en.wikipedia.org/wiki/Horner%27s_method */
/* package */ class HornerScheme implements ScalarUnaryOperator {
  private final Tensor coeffs;

  // careful: the coeffs are in reversed order in comparison to Series
  public HornerScheme(Tensor coeffs) {
    this.coeffs = coeffs;
  }

  @Override
  public Scalar apply(Scalar scalar) {
    Scalar total = scalar.zero();
    for (Tensor entry : coeffs)
      total = total.multiply(scalar).add(entry);
    return total;
  }
}
