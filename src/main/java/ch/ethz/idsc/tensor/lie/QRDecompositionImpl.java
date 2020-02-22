// code by jph
package ch.ethz.idsc.tensor.lie;

import java.io.Serializable;

import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.alg.NormalizeUnlessZero;
import ch.ethz.idsc.tensor.mat.ConjugateTranspose;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Diagonal;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.red.Norm2Squared;
import ch.ethz.idsc.tensor.red.Times;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Conjugate;

/** decomposition Q.R = A with Det[Q] == +1
 * householder with even number of reflections
 * reproduces example on wikipedia */
/* package */ class QRDecompositionImpl implements QRDecomposition, Serializable {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Norm._2);
  // ---
  private final int n;
  private final int m;
  private final Tensor eye;
  private final QRSignOperator qrSignOperator;
  private Tensor Qinv;
  private Tensor R;

  /** @param A
   * @param qrSignOperator
   * @throws Exception if input is not a matrix */
  public QRDecompositionImpl(Tensor A, QRSignOperator qrSignOperator) {
    n = A.length();
    m = Unprotect.dimension1(A);
    eye = IdentityMatrix.of(n);
    this.qrSignOperator = qrSignOperator;
    Qinv = eye;
    R = A;
    // the m-th reflection is necessary in the case where A is non-square
    for (int k = 0; k < m; ++k) {
      Tensor H = reflect(k);
      Qinv = H.dot(Qinv);
      R = H.dot(R);
    }
    // chop lower entries to symbolic zero
    for (int k = 0; k < m; ++k)
      for (int i = k + 1; i < n; ++i)
        R.set(Chop._12, i, k);
  }

  // suggestion of wikipedia
  private Tensor reflect(final int k) {
    Tensor x = Tensors.vector(i -> i < k ? RealScalar.ZERO : R.get(i, k), n);
    Scalar xn = Norm._2.ofVector(x);
    if (Scalars.isZero(xn))
      return eye; // reflection reduces to identity, hopefully => det == 0
    Scalar sign = qrSignOperator.of(R.Get(k, k));
    x.set(value -> value.subtract(sign.multiply(xn)), k);
    final Tensor m;
    if (ExactTensorQ.of(x)) {
      Scalar norm2squared = Norm2Squared.ofVector(x);
      m = Scalars.isZero(norm2squared) //
          ? TensorProduct.of(x, x)
          : TensorProduct.of(x, Conjugate.of(x.add(x)).divide(norm2squared));
    } else {
      Tensor v = NORMALIZE_UNLESS_ZERO.apply(x);
      m = TensorProduct.of(v, Conjugate.of(v.add(v)));
    }
    Tensor r = eye.subtract(m);
    r.set(Tensor::negate, k); // 2nd reflection
    return r;
  }

  @Override // from QRDecomposition
  public Tensor getInverseQ() {
    return Qinv;
  }

  @Override // from QRDecomposition
  public Tensor getR() {
    return R;
  }

  @Override // from QRDecomposition
  public Tensor getQ() {
    return ConjugateTranspose.of(Qinv);
  }

  @Override // from QRDecomposition
  public Scalar det() {
    // FIXME the determinant is only valid up to sign!
    return n == m //
        ? Times.pmul(Diagonal.of(R)).Get()
        : RealScalar.ZERO;
  }
}
