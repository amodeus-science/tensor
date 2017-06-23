// code by jph
package ch.ethz.idsc.tensor;

import ch.ethz.idsc.tensor.sca.MachineNumberQInterface;

/** implementation consistent with Mathematica
 * 
 * MachineNumberQ[ 3.14 + 2.7*I ] == true
 * MachineNumberQ[ 13 / 17 ] == false
 * 
 * <p>Special cases are
 * MachineNumberQ[Infinity] == false
 * MachineNumberQ[Indeterminate] == false
 * 
 * <p>see also {@link ExactNumberQ}
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/MachineNumberQ.html">MachineNumberQ</a> */
public enum MachineNumberQ {
  ;
  /** @param tensor
   * @return true, if tensor is instance of {@link MachineNumberQInterface} which evaluates to true,
   * otherwise false */
  public static boolean of(Tensor tensor) {
    if (tensor instanceof MachineNumberQInterface) {
      MachineNumberQInterface machineNumberQInterface = (MachineNumberQInterface) tensor;
      return machineNumberQInterface.isMachineNumber();
    }
    return false;
  }
}
