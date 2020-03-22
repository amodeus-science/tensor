// code by jph
package ch.ethz.idsc.tensor.lie;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.MatrixQ;

/** class provides ad-tensors of several low-dimensional Lie-algebras */
public enum LieAlgebras {
  ;
  private static final Scalar P1 = RealScalar.ONE;
  private static final Scalar N1 = RealScalar.ONE.negate();
  private static final Scalar P2 = RealScalar.of(+2);
  private static final Scalar N2 = RealScalar.of(-2);
  private static final Tensor SO3 = LeviCivitaTensor.of(3).negate().unmodifiable();

  /** @param x square matrix
   * @param y square matrix
   * @return Lie-bracket [x, y] == x.y - y.x
   * @throws Exception if x or y are not square matrices */
  public static Tensor bracketMatrix(Tensor x, Tensor y) {
    return MatrixQ.require(x).dot(y).subtract(MatrixQ.require(y).dot(x));
  }

  /** @return ad tensor of 3-dimensional Heisenberg Lie-algebra */
  public static Tensor he1() {
    Tensor ad = Array.zeros(3, 3, 3);
    ad.set(P1, 2, 1, 0);
    ad.set(N1, 2, 0, 1);
    return ad;
  }

  /** @return ad tensor of 3-dimensional so(3) */
  public static Tensor so3() {
    return SO3.copy();
  }

  /** @return ad tensor of 3-dimensional sl(2) */
  public static Tensor sl2() {
    Tensor ad = Array.zeros(3, 3, 3);
    ad.set(P1, 1, 1, 0);
    ad.set(N1, 1, 0, 1);
    ad.set(N1, 2, 2, 0);
    ad.set(P1, 2, 0, 2);
    ad.set(P2, 0, 2, 1);
    ad.set(N2, 0, 1, 2);
    return ad;
  }

  /** @return ad tensor of 3-dimensional se(2) */
  public static Tensor se2() {
    Tensor ad = Array.zeros(3, 3, 3);
    ad.set(P1, 1, 0, 2);
    ad.set(N1, 1, 2, 0);
    ad.set(N1, 0, 2, 1);
    ad.set(P1, 0, 1, 2);
    return ad;
  }
}
