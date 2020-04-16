// code by jph
package ch.ethz.idsc.tensor.mat;

import java.math.BigInteger;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.BinaryPower;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.sca.Power;

/** Implementation is consistent with Mathematica.
 * 
 * For non-square matrix input:
 * <pre>
 * MatrixPower[{{1, 2}}, 0] => Exception
 * MatrixPower[{{1, 2}}, 1] => Exception
 * </pre>
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/MatrixPower.html">MatrixPower</a> */
public class MatrixPower extends BinaryPower<Tensor> {
  /** @param matrix square
   * @param exponent
   * @return matrix ^ exponent
   * @throws Exception if matrix is not square */
  public static Tensor of(Tensor matrix, long exponent) {
    return of(matrix, BigInteger.valueOf(exponent));
  }

  /** @param matrix square
   * @param exponent
   * @return matrix ^ exponent
   * @throws Exception if matrix is not square */
  public static Tensor of(Tensor matrix, BigInteger exponent) {
    return new MatrixPower(matrix.length()) //
        .apply(SquareMatrixQ.require(matrix), exponent);
  }

  /** @param matrix symmetric
   * @param exponent
   * @return */
  public static Tensor ofSymmetric(Tensor matrix, Scalar exponent) {
    Eigensystem eigensystem = Eigensystem.ofSymmetric(matrix);
    Tensor avec = eigensystem.vectors(); // OrthogonalMatrixQ
    return Transpose.of(avec).dot(eigensystem.values().map(Power.function(exponent)).pmul(avec));
  }

  /***************************************************/
  private final int n;

  private MatrixPower(int n) {
    this.n = n;
  }

  @Override // from BinaryPower
  protected Tensor zeroth() {
    return IdentityMatrix.of(n);
  }

  @Override // from BinaryPower
  protected Tensor invert(Tensor matrix) {
    return Inverse.of(matrix);
  }

  @Override // from BinaryPower
  protected Tensor multiply(Tensor matrix1, Tensor matrix2) {
    return matrix1.dot(matrix2);
  }
}
