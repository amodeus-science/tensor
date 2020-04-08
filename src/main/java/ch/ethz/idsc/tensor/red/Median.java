// code by jph
package ch.ethz.idsc.tensor.red;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Sort;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.InverseCDF;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/Median.html">Median</a> */
public enum Median {
  ;
  /** <code>Median[{1, 2, 3, 4, 5, 6, 7}] == 4</code>
   * <code>Median[{1, 2, 3, 4, 5, 6, 7, 8}] == 9/2</code>
   * 
   * @param tensor unsorted
   * @return */
  public static Tensor of(Tensor tensor) {
    return ofSorted(Sort.of(tensor));
  }

  /** @param sorted vector either ascending or descending
   * @return entry in the center if length is odd, otherwise the average of the two center entries */
  public static Tensor ofSorted(Tensor sorted) {
    int length = sorted.length();
    int index = length / 2;
    return length % 2 == 0 //
        ? Mean.of(sorted.extract(index - 1, index + 1))
        : sorted.get(index);
  }

  /** @param distribution
   * @return median of given probability distribution
   * @throws Exception if distribution does not implement {@link InverseCDF} */
  public static Scalar of(Distribution distribution) {
    return InverseCDF.of(distribution).quantile(RationalScalar.HALF);
  }
}
