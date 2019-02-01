// code by jph
package ch.ethz.idsc.tensor.mat;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class SymmetricMatrixQTest extends TestCase {
  public void testHilbert() {
    assertTrue(SymmetricMatrixQ.of(HilbertMatrix.of(10)));
  }

  public void testVector() {
    assertFalse(SymmetricMatrixQ.of(Tensors.vector(1, 2, 3)));
  }

  public void testScalar() {
    assertFalse(SymmetricMatrixQ.of(RealScalar.ONE));
  }

  public void testFailNull() {
    try {
      SymmetricMatrixQ.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
