// code by jph
package ch.ethz.idsc.tensor.opt;

import java.util.List;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.alg.Last;
import ch.ethz.idsc.tensor.mat.MatrixQ;
import ch.ethz.idsc.tensor.red.Norm;

/** Quote from Wikipedia:
 * The algorithm is widely used in robotics to perform simplification and denoising
 * of range data acquired by a rotating range scanner.
 * In this field it is known as the split-and-merge algorithm and is attributed to Duda and Hart.
 * 
 * The expected complexity of this algorithm is O(n log n).
 * However, the worst-case complexity is O(n^2). */
public enum RamerDouglasPeucker {
  ;
  /** @param tensor
   * @param epsilon
   * @return */
  public static Tensor of(Tensor tensor, Scalar epsilon) {
    if (tensor.length() == 0)
      return Tensors.empty();
    if (!MatrixQ.of(tensor))
      throw TensorRuntimeException.of(tensor);
    List<Integer> dims = Dimensions.of(tensor);
    if (dims.get(1) == 2) {
      if (dims.get(0) == 1)
        return tensor;
      return _of(tensor, epsilon);
    }
    throw TensorRuntimeException.of(tensor);
  }

  private static Tensor _of(Tensor tensor, Scalar epsilon) {
    if (tensor.length() == 2)
      return tensor;
    if (tensor.length() <= 1)
      throw TensorRuntimeException.of(tensor);
    Tensor first = tensor.get(0);
    Tensor last = Last.of(tensor);
    Tensor diff = last.subtract(first);
    Scalar norm = Norm._2.of(diff);
    if (Scalars.isZero(norm)) // TODO not sure what to do when |diff| == 0
      throw TensorRuntimeException.of(tensor);
    Tensor vector = diff.multiply(norm.invert());
    Tensor cross2 = Tensors.of(vector.Get(1).negate(), vector.Get(0));
    Scalar dmax = RealScalar.ZERO;
    int split = -1;
    for (int index = 1; index < tensor.length() - 1; ++index) {
      Tensor lever = tensor.get(index).subtract(first);
      Scalar dist = lever.dot(cross2).Get().abs();
      if (Scalars.lessThan(dmax, dist)) {
        dmax = dist;
        split = index;
      }
    }
    if (Scalars.lessThan(epsilon, dmax)) {
      Tensor lo = _of(tensor.extract(0, split + 1), epsilon);
      Tensor hi = _of(tensor.extract(split, tensor.length()), epsilon);
      return Join.of(lo.extract(0, lo.length() - 1), hi);
    }
    return Tensors.of(first, last);
  }
}
