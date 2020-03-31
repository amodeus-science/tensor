// code by jph
// https://docs.oracle.com/javase/7/docs/api/java/lang/Double.html#valueOf(java.lang.String)
package ch.ethz.idsc.tensor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.sca.ArcTan;
import ch.ethz.idsc.tensor.sca.ComplexEmbedding;
import ch.ethz.idsc.tensor.sca.Sign;

/* package */ enum StaticHelper {
  ;
  static final String OPENING_BRACKET_STRING = Character.toString(Tensor.OPENING_BRACKET);
  static final String CLOSING_BRACKET_STRING = Character.toString(Tensor.CLOSING_BRACKET);
  static final Collector<CharSequence, ?, String> EMBRACE = //
      Collectors.joining(", ", OPENING_BRACKET_STRING, CLOSING_BRACKET_STRING);
  // ---
  /** code from java.lang.Double */
  private static final String Digits = "(\\p{Digit}+)";
  private static final String HexDigits = "(\\p{XDigit}+)";
  // an exponent is 'e' or 'E' followed by an optionally
  // signed decimal integer.
  private static final String Exp = "[eE][+-]?" + Digits;
  // optional leading and trailing whitespace and sign is obsolete
  static final String fpRegex = ("(" + //
      "NaN|" + // "NaN" string
      "Infinity|" + // "Infinity" string
      // A decimal floating-point string representing a finite positive
      // number without a leading sign has at most five basic pieces:
      // Digits . Digits ExponentPart FloatTypeSuffix
      //
      // Since this method allows integer-only strings as input
      // in addition to strings of floating-point literals, the
      // two sub-patterns below are simplifications of the grammar
      // productions from section 3.10.2 of
      // The Java Language Specification.
      // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
      "(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +
      // . Digits ExponentPart_opt FloatTypeSuffix_opt
      "(\\.(" + Digits + ")(" + Exp + ")?)|" +
      // Hexadecimal strings
      "((" +
      // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
      "(0[xX]" + HexDigits + "(\\.)?)|" +
      // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
      "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" + ")[pP][+-]?" + Digits + "))" + "[fFdD]?))" //
  );

  // throws an exception if value is Infinity
  public static BigInteger floor(BigDecimal bigDecimal) {
    BigInteger bigInteger = bigDecimal.toBigInteger();
    if (0 < new BigDecimal(bigInteger).compareTo(bigDecimal)) {
      bigDecimal = bigDecimal.subtract(BigDecimal.ONE);
      bigInteger = bigDecimal.toBigInteger();
    }
    return bigInteger;
  }

  // throws an exception if value is Infinity
  public static BigInteger ceiling(BigDecimal bigDecimal) {
    BigInteger bigInteger = bigDecimal.toBigInteger();
    if (new BigDecimal(bigInteger).compareTo(bigDecimal) < 0) {
      bigDecimal = bigDecimal.add(BigDecimal.ONE);
      bigInteger = bigDecimal.toBigInteger();
    }
    return bigInteger;
  }

  /** @param x complex scalar
   * @param y complex scalar
   * @return Mathematica::ArcTan[x, y] */
  public static Scalar arcTan(Scalar x, Scalar y) {
    if (Scalars.isZero(x)) { // prevent division by zero
      ComplexEmbedding complexEmbedding = (ComplexEmbedding) y;
      return Sign.FUNCTION.apply(complexEmbedding.real()).multiply(Pi.HALF);
    }
    return ArcTan.FUNCTION.apply(y.divide(x));
  }
}
