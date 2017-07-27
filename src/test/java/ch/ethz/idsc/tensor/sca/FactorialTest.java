// code by jph
package ch.ethz.idsc.tensor.sca;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class FactorialTest extends TestCase {
  public void testSimple() {
    assertEquals(Factorial.of(RealScalar.of(0)), RealScalar.of(1));
    assertEquals(Factorial.of(RealScalar.of(1)), RealScalar.of(1));
    assertEquals(Factorial.of(RealScalar.of(2)), RealScalar.of(2));
    assertEquals(Factorial.of(RealScalar.of(3)), RealScalar.of(6));
    assertEquals(Factorial.of(RealScalar.of(4)), RealScalar.of(24));
  }

  public void testOf1() {
    Scalar result = Factorial.of(RealScalar.of(3));
    assertEquals(result, RealScalar.of(6));
  }

  public void testOf2() {
    Tensor result = Factorial.of(Tensors.vector(0, 1, 2, 3, 4));
    assertEquals(result, Tensors.vector(1, 1, 2, 6, 24));
  }

  public void testFail() {
    try {
      Factorial.of(RealScalar.of(-1));
      assertTrue(false);
    } catch (Exception exception) {
      // ---
    }
    try {
      Factorial.of(RealScalar.of(1.2));
      assertTrue(false);
    } catch (Exception exception) {
      // ---
    }
  }
}
