package com.tsien.mall.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tsien
 * @version 1.0.0
 * @date 2019/7/2 0002 17:43
 */

public class BigDecimalUtil {

    private BigDecimalUtil() {

    }

    /**
     * BigDecimal 加法
     *
     * @param value1 value1
     * @param value2 value2
     * @return 结果
     */
    public static BigDecimal add(double value1, double value2) {
        BigDecimal bigDecimal1 = BigDecimal.valueOf(value1);
        BigDecimal bigDecimal2 = BigDecimal.valueOf(value2);
        return bigDecimal1.add(bigDecimal2);

    }

    /**
     * BigDecimal 减法
     *
     * @param value1 value1
     * @param value2 value2
     * @return 结果
     */
    public static BigDecimal subtract(double value1, double value2) {
        BigDecimal bigDecimal1 = BigDecimal.valueOf(value1);
        BigDecimal bigDecimal2 = BigDecimal.valueOf(value2);
        return bigDecimal1.subtract(bigDecimal2);

    }


    /**
     * BigDecimal 乘法
     *
     * @param value1 value1
     * @param value2 value2
     * @return 结果
     */
    public static BigDecimal multiply(double value1, double value2) {
        BigDecimal bigDecimal1 = BigDecimal.valueOf(value1);
        BigDecimal bigDecimal2 = BigDecimal.valueOf(value2);
        return bigDecimal1.multiply(bigDecimal2);

    }

    /**
     * BigDecimal 除法
     *
     * @param value1 value1
     * @param value2 value2
     * @return 结果
     */
    public static BigDecimal divide(double value1, double value2) {
        BigDecimal bigDecimal1 = BigDecimal.valueOf(value1);
        BigDecimal bigDecimal2 = BigDecimal.valueOf(value2);
        return bigDecimal1.divide(bigDecimal2, 2, RoundingMode.HALF_UP);

    }

}
