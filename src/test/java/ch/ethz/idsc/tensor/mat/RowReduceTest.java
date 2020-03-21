// code by jph
package ch.ethz.idsc.tensor.mat;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.alg.Reverse;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.N;
import junit.framework.TestCase;

public class RowReduceTest extends TestCase {
  // from Mathematica, RowReduce Applications: solving a linear system
  public void testReduce1() {
    Tensor matrix = Tensors.fromString("{{1, 2, 3, 1}, {5, 6, 7, 1}, {7, 8, 9, 1}}");
    Tensor reduce = RowReduce.of(matrix);
    Tensor sol = Tensors.fromString("{{1, 0, -1, -1}, {0, 1, 2, 1}, {0, 0, 0, 0}}");
    assertEquals(reduce, sol);
  }

  // from Mathematica, RowReduce Applications: a linear system without solution
  public void testReduce2() {
    Tensor matrix = Tensors.fromString("{{1, 2, 3, 1}, {5, 6, 7, -2}, {7, 8, 9, 1}}");
    Tensor reduce = RowReduce.of(matrix);
    Tensor sol = Tensors.fromString("{{1, 0, -1, 0}, {0, 1, 2, 0}, {0, 0, 0, 1}}");
    assertEquals(reduce, sol);
  }

  // from Mathematica, RowReduce Applications: for a degenerate square matrix
  public void testReduce3() {
    Tensor matrix = Tensors.fromString("{{1, 2, 3, 4, 1, 0, 0, 0}, {5, 6, 7, 8, 0, 1, 0, 0}, {9, 10, 11, 12, 0, 0, 1, 0}, {13, 14, 15, 16, 0, 0, 0, 1}}");
    Tensor reduce = RowReduce.of(matrix);
    Tensor sol = Tensors.fromString("{{1, 0, -1, -2, 0, 0, -7/2, 5/2}, {0, 1, 2, 3, 0, 0, 13/4, -9/4}, {0, 0, 0, 0, 1, 0, -3, 2}, {0, 0, 0, 0, 0, 1, -2, 1}}");
    assertEquals(reduce, sol);
  }

  public void testSome() {
    Tensor A = Tensors.matrix(new Number[][] { //
        { -1, -2, -1 }, //
        { -3, 1, 5 }, //
        { 3, 6, 3 }, //
        { 1, 2, 1 } //
    });
    Tensor r = RowReduce.of(A);
    assertEquals(Dimensions.of(r), Dimensions.of(A));
  }

  public void testReduce2N() {
    Tensor matrix = N.DOUBLE.of(Tensors.fromString("{{1, 2, 3, 1}, {5, 6, 7, -2}, {7, 8, 9, 1}}"));
    Tensor reduce = RowReduce.of(matrix);
    Tensor sol = Tensors.fromString("{{1, 0, -1, 0}, {0, 1, 2, 0}, {0, 0, 0, 1}}");
    Tolerance.CHOP.requireClose(reduce, sol);
  }

  public void testQuantity1() {
    Tensor ve1 = Tensors.of(Quantity.of(1, "m"), Quantity.of(2, "m"));
    Tensor ve2 = Tensors.of(Quantity.of(2, "m"), Quantity.of(10, "m"));
    Tensor nul = RowReduce.of(Tensors.of(ve1, ve2));
    assertEquals(nul, IdentityMatrix.of(2)); // consistent with Mathematica
  }

  public void testQuantity2() {
    Tensor ve1 = Tensors.of(Quantity.of(1, "m"), Quantity.of(2, "m"));
    Tensor nul = RowReduce.of(Tensors.of(ve1, ve1));
    assertEquals(nul, Tensors.fromString("{{1, 2}, {0[m], 0[m]}}"));
    assertTrue(Chop.NONE.close(nul, Tensors.fromString("{{1, 2}, {0, 0}}")));
  }

  public void testPivots() {
    Tensor matrix = HilbertMatrix.of(3, 5);
    Tensor gf1 = RowReduce.of(matrix);
    Tensor gf2 = RowReduce.of(Reverse.of(matrix), PivotFirstNonZero.INSTANCE);
    assertEquals(gf1, gf2);
  }

  public void testUnstructuredFail() {
    Tensor matrix = Tensors.fromString("{{}, {2, 3}}");
    try {
      RowReduce.of(matrix);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
