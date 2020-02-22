// code by jph
package ch.ethz.idsc.tensor.mat;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.lie.LieAlgebras;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class OrthogonalMatrixQTest extends TestCase {
  public void testExact() {
    Tensor matrix = Tensors.fromString("{{1, 1, 1, -1}, {-1, 1, 1, 1}}").multiply(RationalScalar.of(1, 2));
    assertTrue(OrthogonalMatrixQ.of(matrix));
    OrthogonalMatrixQ.require(matrix, Chop.NONE);
  }

  public void testDetNegative() {
    Tensor matrix = DiagonalMatrix.of(-1, 1, 1, -1, -1);
    OrthogonalMatrixQ.require(matrix);
    assertEquals(Det.of(matrix), RealScalar.ONE.negate());
  }

  public void testCornerCase() {
    assertFalse(OrthogonalMatrixQ.of(RealScalar.of(1)));
    assertFalse(OrthogonalMatrixQ.of(Tensors.vector(1, 0, 0)));
    assertFalse(OrthogonalMatrixQ.of(Tensors.vector(1, 0, 2)));
    assertFalse(OrthogonalMatrixQ.of(LieAlgebras.so3()));
  }

  public void testRequireChop() {
    OrthogonalMatrixQ.require(IdentityMatrix.of(4), Chop.NONE);
    try {
      OrthogonalMatrixQ.require(HilbertMatrix.of(3), Chop.NONE);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testRequire() {
    OrthogonalMatrixQ.require(IdentityMatrix.of(4));
    try {
      OrthogonalMatrixQ.require(HilbertMatrix.of(3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
