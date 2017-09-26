// code by jph
package ch.ethz.idsc.tensor.mat;

import java.util.stream.IntStream;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.ArgMax;

/** selects entry with largest absolute value */
/* package */ enum PivotArgMaxAbs implements Pivot {
  INSTANCE;
  @Override // from Pivot
  public int get(int c0, int j, int[] ind, Tensor lhs) {
    return c0 + ArgMax.of( //
        Tensor.of(IntStream.range(c0, ind.length) //
            .mapToObj(c1 -> lhs.Get(ind[c1], j).abs()) //
            .map(Scalar::number) // projection to Number is required for scalars with units
            .map(RealScalar::of))); // wrap as RealScalar for use in ArgMax
  }
}