// code by jph
package ch.ethz.idsc.tensor.mat;

import java.util.stream.IntStream;

import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;

/** picks the first non-zero element in the column as pivot
 * the return value is c0 in the case when the element at (ind[c0], j)
 * is non-zero, but also if none of the candidates is non-zero */
/* package */ enum PivotFirstNonZero implements Pivot {
  INSTANCE;

  @Override // from Pivot
  public int get(int row, int col, int[] ind, Tensor[] lhs) {
    return IntStream.range(row, ind.length) //
        .filter(c1 -> Scalars.nonZero(lhs[ind[c1]].Get(col))) //
        .findFirst().orElse(row);
  }
}
