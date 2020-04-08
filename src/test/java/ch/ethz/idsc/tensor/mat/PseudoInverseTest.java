// code by jph
package ch.ethz.idsc.tensor.mat;

import java.util.Arrays;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class PseudoInverseTest extends TestCase {
  public void testHilbertSquare() {
    for (int n = 1; n < 8; ++n) {
      Tensor matrix = HilbertMatrix.of(n);
      Tensor result = PseudoInverse.of(matrix);
      Tensor identy = IdentityMatrix.of(n);
      Chop._06.requireClose(result.dot(matrix), identy);
      Chop._06.requireClose(matrix.dot(result), identy);
    }
  }

  public void testHilbert46() {
    for (int n = 1; n < 6; ++n) {
      Tensor matrix = HilbertMatrix.of(n, 6);
      Tensor result = PseudoInverse.of(matrix);
      assertEquals(Dimensions.of(result), Arrays.asList(6, n));
      Chop._10.requireClose(matrix.dot(result), IdentityMatrix.of(n));
    }
  }

  public void testMathematica1() {
    Tensor matrix = Tensors.fromString("{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}}");
    Tensor result = PseudoInverse.of(matrix);
    Tensor actual = Tensors.fromString("{{-(29/60), -(11/45), -(1/180), 7/30}, {-(1/30), -(1/90), 1/90, 1/30}, {5/12, 2/9, 1/36, -(1/6)}}");
    Chop._12.requireClose(result.subtract(actual), Array.zeros(3, 4));
  }

  public void testMathematica2() {
    Tensor matrix = Transpose.of(Tensors.fromString("{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {10, 11, 12}}"));
    Tensor result = PseudoInverse.of(matrix);
    Tensor actual = Transpose.of(Tensors.fromString("{{-(29/60), -(11/45), -(1/180), 7/30}, {-(1/30), -(1/90), 1/90, 1/30}, {5/12, 2/9, 1/36, -(1/6)}}"));
    Chop._12.requireClose(result.subtract(actual), Array.zeros(4, 3));
  }

  public void testQuantity() {
    Distribution distribution = NormalDistribution.of(Quantity.of(0, "m"), Quantity.of(1, "m"));
    for (int n = 1; n < 7; ++n) {
      Tensor matrix = RandomVariate.of(distribution, n, n);
      Chop._09.requireClose(Inverse.of(matrix), PseudoInverse.of(matrix));
    }
  }

  public void testEmptyMatrixFail() {
    try {
      PseudoInverse.of(Tensors.of(Tensors.empty()));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testEmptyFail() {
    try {
      PseudoInverse.of(Tensors.empty());
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testScalarFail() {
    try {
      PseudoInverse.of(RealScalar.ONE);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
