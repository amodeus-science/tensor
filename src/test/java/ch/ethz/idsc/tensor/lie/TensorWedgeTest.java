// code by jph
package ch.ethz.idsc.tensor.lie;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.mat.AntisymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class TensorWedgeTest extends TestCase {
  public void testAntisymmetric() {
    Tensor matrix = RandomVariate.of(NormalDistribution.standard(), 4, 4);
    Tensor skewsy = TensorWedge.of(matrix);
    assertTrue(AntisymmetricMatrixQ.of(skewsy));
  }

  public void testCreate() {
    Tensor matrix = Tensors.fromString("{{1, 2}, {0, 4}}");
    Tensor wedged = TensorWedge.of(matrix);
    assertEquals(wedged, Tensors.fromString("{{0, 1}, {-1, 0}}"));
  }

  public void testVector() {
    Tensor vector = RandomVariate.of(NormalDistribution.standard(), 10);
    Tensor skewsy = TensorWedge.of(vector);
    assertEquals(vector, skewsy);
  }

  public void testScalar() {
    Tensor scalar = RealScalar.of(3.14);
    assertEquals(scalar, TensorWedge.of(scalar));
  }

  public void testAlternating() {
    Tensor alt = LieAlgebras.so3();
    assertEquals(alt, TensorWedge.of(alt));
  }

  public void testSome() {
    Tensor vector = Tensors.vector(2, 3, 4);
    Tensor skewsy = TensorWedge.of(vector, Tensors.vector(1, 1, 1));
    assertTrue(AntisymmetricMatrixQ.of(skewsy));
  }

  public void testCross() {
    Tensor vector = Tensors.vector(2, 3, 4);
    Tensor matrix = Cross.skew3(vector);
    assertTrue(AntisymmetricMatrixQ.of(matrix));
    assertEquals(matrix, TensorWedge.of(matrix));
  }

  public void testFailIrrectangular() {
    Tensor matrix = Tensors.fromString("{{1, 2}, {0, 4, 3}}");
    try {
      TensorWedge.of(matrix);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFailRectangularMatrix() {
    try {
      TensorWedge.of(HilbertMatrix.of(3, 4));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFailRectangularArray() {
    try {
      TensorWedge.of(Array.zeros(2, 2, 3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFailLength() {
    TensorWedge.of(Array.zeros(3), Array.zeros(3));
    try {
      TensorWedge.of(Array.zeros(3), Array.zeros(4));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
