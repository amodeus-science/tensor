// code by jph
package ch.ethz.idsc.tensor.alg;

import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class TensorComparatorTest extends TestCase {
  public void testSimple() {
    assertEquals(TensorComparator.INSTANCE.compare( //
        Tensors.vector(2, 1), Tensors.vector(2, 1, 3)), Integer.compare(1, 2));
    assertEquals(TensorComparator.INSTANCE.compare( //
        Tensors.vector(2, 1), Tensors.vector(1, 2, 3)), Integer.compare(1, 2));
  }
}
