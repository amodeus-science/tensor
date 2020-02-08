// code by jph
package ch.ethz.idsc.tensor.mat;

import java.util.stream.IntStream;

import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.MachineNumberQ;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.lie.QRDecomposition;
import ch.ethz.idsc.tensor.sca.Chop;

/** {@link NullSpace#of(Tensor)} picks the most suited algorithm to determine the
 * nullspace of a given matrix.
 * 
 * <p>Three methods are available:
 * 
 * <ul>
 * <li>{@link NullSpace#usingRowReduce(Tensor)}
 * <li>{@link NullSpace#usingQR(Tensor)}
 * <li>{@link NullSpace#usingSvd(Tensor)}
 * </ul>
 * 
 * <p>Let N = NullSpace[V]. If N is non-empty, then V.Transpose[N] == 0.
 * 
 * <p>Quote from Wikipedia:
 * For matrices whose entries are floating-point numbers, the problem of computing the kernel
 * makes sense only for matrices such that the number of rows is equal to their rank:
 * because of the rounding errors, a floating-point matrix has almost always a full rank,
 * even when it is an approximation of a matrix of a much smaller rank. Even for a full-rank
 * matrix, it is possible to compute its kernel only if it is well conditioned, i.e. it has a
 * low condition number.
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/NullSpace.html">NullSpace</a> */
public enum NullSpace {
  ;
  /** if matrix has any entry in machine precision, i.e. {@link MachineNumberQ} returns true,
   * the nullspace is computed using {@link SingularValueDecomposition}.
   * In that case the vectors in the return value are normalized.
   * 
   * <p>If all entries of the given matrix are in exact precision,
   * the nullspace is computed using {@link RowReduce}.
   * In that case the vectors in the return value are <em>not</em> normalized.
   * 
   * <p>Function is consistent with Mathematica.
   * 
   * @param matrix
   * @return list of vectors that span the nullspace; if the nullspace is trivial, then
   * the return value is the empty tensor {}
   * @throws Exception if given parameter is not a matrix */
  public static Tensor of(Tensor matrix) {
    if (ExactTensorQ.of(matrix))
      return usingRowReduce(matrix);
    int rows = matrix.length();
    int cols = Unprotect.dimension1(matrix);
    return rows < cols //
        ? usingQR(matrix)
        : usingSvd(matrix);
  }

  /** @param matrix with exact precision entries
   * @return tensor of vectors that span the kernel of given matrix */
  public static Tensor usingRowReduce(Tensor matrix) {
    return _usingRowReduce(matrix, IdentityMatrix.of(Unprotect.dimension1(matrix)));
  }

  /** @param matrix of dimensions n x m with exact precision entries
   * @param identity matrix of dimensions m x m that contains the unit vector
   * for the scalar type of each column
   * @return tensor of vectors that span the kernel of given matrix */
  public static Tensor usingRowReduce(Tensor matrix, Tensor identity) {
    return _usingRowReduce(matrix, SquareMatrixQ.require(identity));
  }

  // helper function
  private static Tensor _usingRowReduce(Tensor matrix, Tensor identity) {
    final int n = matrix.length();
    final int m = identity.length();
    Tensor lhs = RowReduce.of(Join.of(1, Transpose.of(matrix), identity));
    int j = 0;
    int c0 = 0;
    while (c0 < n)
      if (Scalars.nonZero(lhs.Get(j, c0++))) // <- careful: c0 is modified
        ++j;
    return Tensor.of(lhs.extract(j, m).stream().map(row -> row.extract(n, n + m)));
  }

  /** @param matrix
   * @return list of orthogonal vectors that span the nullspace */
  public static Tensor usingQR(Tensor matrix) {
    Tensor mt = Transpose.of(matrix);
    int d = Unprotect.dimension1(mt);
    QRDecomposition qrDecomposition = QRDecomposition.of(mt);
    // TODO logic below is yet too simple
    return Tensor.of(qrDecomposition.getInverseQ().stream().skip(d));
  }

  /** @param matrix of dimensions rows x cols with rows >= cols
   * @return (cols - rank()) x cols matrix
   * @throws Exception if given matrix has rows &lt; cols */
  public static Tensor usingSvd(Tensor matrix) {
    return of(SingularValueDecomposition.of(matrix));
  }

  /** @param svd
   * @param chop
   * @return tensor of vectors that span the kernel of given matrix */
  public static Tensor of(SingularValueDecomposition svd, Chop chop) {
    Tensor vt = Transpose.of(svd.getV());
    return Tensor.of(IntStream.range(0, svd.values().length()) //
        .filter(index -> Scalars.isZero(chop.apply(svd.values().Get(index)))) //
        .mapToObj(vt::get));
  }

  /** @param svd
   * @return tensor of vectors that span the kernel of given matrix */
  public static Tensor of(SingularValueDecomposition svd) {
    return of(svd, Tolerance.CHOP);
  }
}
