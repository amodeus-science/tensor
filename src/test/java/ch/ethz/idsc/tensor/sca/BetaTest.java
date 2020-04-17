// code by jph
package ch.ethz.idsc.tensor.sca;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import junit.framework.TestCase;

public class BetaTest extends TestCase {
  public void testExact() {
    Scalar beta = Beta.of(5, 4);
    Scalar exact = RationalScalar.of(1, 280);
    Chop._14.requireClose(beta, exact);
  }

  public void testNumeric() {
    Scalar beta = Beta.of(2.3, 3.2);
    Scalar exact = RealScalar.of(0.05402979174835722);
    Chop._14.requireClose(beta, exact);
  }

  public void testVectorFail() {
    try {
      Beta.of(HilbertMatrix.of(3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
