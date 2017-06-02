// code by jph
// adapted from wikipedia - Cholesky decomposition
package ch.ethz.idsc.tensor.mat;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Conjugate;

/* package */ class CholeskyDecompositionImpl implements CholeskyDecomposition {
  private final Tensor l;
  private final Tensor d;

  /** @param A hermitian matrix */
  CholeskyDecompositionImpl(Tensor A) {
    final int n = A.length();
    l = IdentityMatrix.of(n);
    d = Array.zeros(n);
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < i; ++j) {
        Tensor lik = l.get(i).extract(0, j);
        Tensor ljk = l.get(j).extract(0, j).map(Conjugate.function);
        Scalar value = A.Get(i, j).subtract(lik.dot(d.extract(0, j).pmul(ljk)));
        if (!value.equals(value.zero()))
          l.set(value.divide(d.Get(j)), i, j);
      }
      Tensor lik = l.get(i).extract(0, i);
      Tensor ljk = l.get(i).extract(0, i).map(Conjugate.function); // variable name is deliberate
      d.set(A.Get(i, i).subtract(lik.dot(d.extract(0, i).pmul(ljk))), i);
    }
  }

  @Override
  public Tensor getL() {
    return l;
  }

  @Override
  public Tensor diagonal() {
    return d;
  }

  @Override
  public Scalar det() {
    return Total.prod(d).Get();
  }
}
