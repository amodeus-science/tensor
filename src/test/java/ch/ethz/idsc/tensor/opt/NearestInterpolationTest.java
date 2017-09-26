// code by jph
package ch.ethz.idsc.tensor.opt;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.ResourceData;
import ch.ethz.idsc.tensor.sca.Floor;
import junit.framework.TestCase;

public class NearestInterpolationTest extends TestCase {
  public void testEmpty() {
    Interpolation interpolation = NearestInterpolation.of(Tensors.empty());
    assertEquals(interpolation.get(Tensors.empty()), Tensors.empty());
  }

  public void testSimple() {
    Interpolation interpolation = NearestInterpolation.of(Tensors.vector(10, 20, 30, 40));
    assertEquals(interpolation.get(Tensors.vector(2.8)), RealScalar.of(40));
    assertEquals(interpolation.get(Tensors.vector(1.1)), RealScalar.of(20));
  }

  public void testFloor() {
    Interpolation interpolation = MappedInterpolation.of(Tensors.vector(10, 20, 30, 40), Floor::of);
    assertEquals(interpolation.get(Tensors.vector(2.8)), RealScalar.of(30));
    assertEquals(interpolation.get(Tensors.vector(1.1)), RealScalar.of(20));
  }

  public void testFail() {
    try {
      NearestInterpolation.of(ResourceData.of("/colorscheme_nocan/hue.csv"));
      assertTrue(false);
    } catch (Exception exception) {
      // ---
    }
  }
}