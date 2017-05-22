// code by jph
package ch.ethz.idsc.tensor.mat;

import java.io.Serializable;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;

/** the Cholesky decomposition of a hermitian matrix establishes matrices L and D with
 * 
 * matrix == L . D . L*
 * 
 * matrix == getL().dot(getD().pmul(ConjugateTranspose.of(getL())))
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/CholeskyDecomposition.html">CholeskyDecomposition</a> */
public interface CholeskyDecomposition extends Serializable {
  /** @param matrix hermitian
   * @return Cholesky decomposition of matrix
   * @throws TensorRuntimeException if matrix is not hermitian, or decomposition cannot be established
   * @see HermitianMatrixQ */
  static CholeskyDecomposition of(Tensor matrix) {
    if (!HermitianMatrixQ.of(matrix))
      throw TensorRuntimeException.of(matrix);
    return new CholeskyDecompositionImpl(matrix);
  }

  /** @return lower triangular matrix L */
  Tensor getL();

  /** @return vector of diagonal entries of D */
  Tensor diagonal();

  /** @return determinant of matrix */
  Scalar det();
}