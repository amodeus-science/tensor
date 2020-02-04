// code by jph
package ch.ethz.idsc.tensor.mat;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.InvertUnlessZero;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/PseudoInverse.html">PseudoInverse</a>
 *
 * @see Inverse
 * @see LeastSquares */
public enum PseudoInverse {
  ;
  /** @param matrix
   * @return pseudoinverse of given matrix */
  public static Tensor of(Tensor matrix) {
    return Unprotect.dimension1(matrix) <= matrix.length() //
        ? of(SingularValueDecomposition.of(matrix)) //
        : Transpose.of(of(Transpose.of(matrix)));
  }

  /** @param svd
   * @param chop
   * @return pseudoinverse of matrix determined by given svd */
  public static Tensor of(SingularValueDecomposition svd, Chop chop) {
    Tensor wi = svd.values().map(InvertUnlessZero.FUNCTION.compose(chop));
    return Tensor.of(svd.getV().stream().map(wi::pmul)).dot(Transpose.of(svd.getU()));
  }

  /** @param svd
   * @return pseudoinverse of matrix determined by given svd */
  public static Tensor of(SingularValueDecomposition svd) {
    return of(svd, Tolerance.CHOP);
  }
}
