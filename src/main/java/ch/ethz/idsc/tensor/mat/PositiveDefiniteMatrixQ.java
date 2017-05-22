// code by jph
package ch.ethz.idsc.tensor.mat;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.ZeroScalar;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/PositiveDefiniteMatrixQ.html">PositiveDefiniteMatrixQ</a> */
public enum PositiveDefiniteMatrixQ {
  ;
  /** @param matrix hermitian
   * @return true if matrix is positive definite
   * @throws TensorRuntimeException if input is not a hermitian matrix */
  public static boolean ofHermitian(Tensor matrix) {
    return !CholeskyDecomposition.of(matrix).diagonal().flatten(0) //
        .map(Scalar.class::cast) //
        .filter(scalar -> Scalars.lessEquals(scalar, ZeroScalar.get())) //
        .findAny().isPresent();
  }
}