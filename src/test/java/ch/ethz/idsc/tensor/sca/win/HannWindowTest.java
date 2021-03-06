// code by jph
package ch.ethz.idsc.tensor.sca.win;

import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class HannWindowTest extends TestCase {
  public void testExact() {
    ScalarUnaryOperator scalarUnaryOperator = HannWindow.FUNCTION;
    assertEquals(scalarUnaryOperator.apply(RealScalar.ZERO), RealScalar.ONE);
    assertEquals(scalarUnaryOperator.apply(RationalScalar.of(+1, 3)), RationalScalar.of(1, 4));
    assertEquals(scalarUnaryOperator.apply(RationalScalar.of(+1, 4)), RationalScalar.of(1, 2));
    assertEquals(scalarUnaryOperator.apply(RationalScalar.of(+1, 6)), RationalScalar.of(3, 4));
    assertEquals(scalarUnaryOperator.apply(RationalScalar.of(-1, 3)), RationalScalar.of(1, 4));
    assertEquals(scalarUnaryOperator.apply(RationalScalar.of(-1, 4)), RationalScalar.of(1, 2));
    assertEquals(scalarUnaryOperator.apply(RationalScalar.of(-1, 6)), RationalScalar.of(3, 4));
  }

  public void testExactFallback() {
    ScalarUnaryOperator scalarUnaryOperator = HannWindow.FUNCTION;
    Scalar scalar = scalarUnaryOperator.apply(RationalScalar.of(1, 7));
    assertEquals(scalar, RealScalar.of(0.8117449009293667));
  }

  public void testZero() {
    ScalarUnaryOperator scalarUnaryOperator = HannWindow.FUNCTION;
    assertEquals(scalarUnaryOperator.apply(RationalScalar.of(7, 12)), RealScalar.ZERO);
  }

  public void testNumeric() {
    ScalarUnaryOperator scalarUnaryOperator = HannWindow.FUNCTION;
    assertEquals(scalarUnaryOperator.apply(RealScalar.of(0.25)), RationalScalar.HALF);
  }

  public void testSemiExact() {
    Scalar scalar = HannWindow.FUNCTION.apply(RealScalar.of(0.5));
    assertTrue(Scalars.isZero(scalar));
    assertTrue(ExactScalarQ.of(scalar));
  }

  public void testOf() {
    Tensor tensor = RandomVariate.of(NormalDistribution.standard(), 2, 3);
    assertEquals(HannWindow.of(tensor), tensor.map(HannWindow.FUNCTION));
  }

  public void testQuantityFail() {
    try {
      HannWindow.FUNCTION.apply(Quantity.of(2, "s"));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
