// code by jph
package ch.ethz.idsc.tensor.mat;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dot;
import ch.ethz.idsc.tensor.qty.Quantity;
import junit.framework.TestCase;

public class DiagonalMatrixTest extends TestCase {
  public void testIdentity() {
    Tensor m = IdentityMatrix.of(10);
    Tensor d = Dot.of(m, m, m, m);
    assertEquals(m, d);
    Tensor e = DiagonalMatrix.of(Tensors.vector(1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
    assertEquals(m, e);
  }

  public void testMisc1() {
    Tensor m = DiagonalMatrix.of(-2, 3, -4);
    assertEquals(Det.of(m).number(), 2 * 3 * 4);
  }

  public void testMisc2() {
    Tensor m = DiagonalMatrix.of( //
        RealScalar.of(-2), RealScalar.of(3), RealScalar.of(-4));
    assertEquals(Det.of(m).number(), 2 * 3 * 4);
  }

  public void testMisc3() {
    try {
      Tensor tensor = RealScalar.of(-2);
      DiagonalMatrix.of(tensor);
      assertTrue(false);
    } catch (Exception exception) {
      // ---
    }
  }

  public void testQuantity() {
    Scalar qs1 = Quantity.of(1, "m");
    Scalar qs2 = Quantity.of(2, "s");
    Tensor vec = Tensors.of(qs1, qs2);
    DiagonalMatrix.of(vec);
  }
}