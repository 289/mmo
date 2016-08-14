package mmo.util;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtil {

    /**
     * 获取Random实例，实际获取的是当前线程持有的ThreadLocalRandom实例
     * 多线程环境下不要使用共享Random实例，应当使用ThreadLocalRandom
     *
     * @return
     */
    public static Random getRandom() {
        return ThreadLocalRandom.current();
    }

    /**
     * 生成 [0,max)范围内的随机整数
     *
     * @param max
     * @return 如果max<0,则返回 0;
     */
    public static int getRandomInt(int max) {
        if (max <= 0) {
            return 0;
        }
        return getRandom().nextInt(max);
    }

    /**
     * @param goal
     * @return
     */
    public static boolean isHit(float goal) {
        int temp = (int) (goal * 100);
        int hit = getRandomInt(1, 10000);
        if (temp >= hit) {
            return true;
        }
        return false;
    }

    /**
     * 返回[min,max]范围内的随机整数
     *
     * @param min
     * @param max 包括max
     * @return
     */
    public static int getRandomInt(int min, int max) {
        if (max < min) {
            throw new IllegalArgumentException("max must greater than min");
        }
        /**
         * 将[min,max]转换为[min-min,max-min],得到[min-min,max-min]范围
         * 内的一个随机数：N,再将N转换为[min,max]范围内对应的数值:N+min
         */
        int seed = max - min + 1;
        int hit = getRandom().nextInt(seed);
        return hit + min;
    }

    /**
     * 暂时先这么写，需要修正
     *
     * @param min
     * @param max
     * @return
     */
    public static float getRandomFloat(float min, float max) {
        if (max < min) {
            throw new IllegalArgumentException("max must greater than min");
        }
        float seed = max - min;
        float hit = getRandom().nextFloat() * seed;
        return formatFloat(hit + min);
    }

    /**
     * 一个工具方法，格式化float,输入1.234,输出1.2,不四舍五入
     *
     * @param src
     * @return
     */
    public static float formatFloat(float src) {
        DecimalFormat dcmFmt = new DecimalFormat("0.0");
        String r = dcmFmt.format(src);
        return Float.valueOf(r);
    }

    /**
     * 获取一个在min-max之间的double数
     *
     * @param min
     * @param max
     * @return
     */
    public static double getRandomDouble(double min, double max) {
        if (max < min) {
            throw new IllegalArgumentException("max must greater than min");
        }
        double seed = max - min;
        double hit = getRandom().nextDouble() * seed;
        return hit + min;
    }

    /**
     * 通过几率，返回是否成功，一个通用方法，因为很多地方用到
     * 万分比
     *
     * @param rate
     * @return
     */
    public static boolean isRandomSucc(int rate) {
        if (rate >= 10000)
            return true;
        int randomInt = getRandomInt(1, 10000);
        if (randomInt <= rate)
            return true;
        return false;
    }

    /**
     * 通过几率，返回是否成功，一个通用方法，因为很多地方用到
     * 百万分比
     *
     * @param rate
     * @return
     */
    public static boolean isMillionRandomSuccess(int rate) {
        if (rate >= 1000000)
            return true;
        int randomInt = getRandomInt(1, 1000000);
        if (randomInt <= rate)
            return true;
        return false;
    }

    /**
     * 通过几率，返回是否成功，一个通用方法，因为很多地方用到
     * 万分比
     *
     * @param rate
     * @return
     */
    public static boolean isRandomSucc100(int rate) {
        if (rate >= 100)
            return true;
        int randomInt = getRandomInt(1, 100);
        if (randomInt <= rate)
            return true;
        return false;
    }

    /**
     * 向上取整
     *
     * @param f
     * @return
     */
    public static int ceil(final float f) {
        final int i = (int) f;
        return i == f ? i : i + 1;
    }

    public static int ceil(final int a, final int b) {
        return (a + b - 1) / b;
    }

    public static int getRandomIndex(Integer[] rateArray) {
        return getRandomIndex(rateArray, total(rateArray));
    }

    public static int total(Integer[] rateArray) {
        int totalRateNum = 0;
        for (int i = 0; i < rateArray.length; i++) {
            int rateNum = rateArray[i];
            totalRateNum += rateNum;
        }
        return totalRateNum;
    }

    public static int getRandomIndex(Integer[] rateArray, int totalRateNum) {
        int randNum = getRandomInt(0, totalRateNum - 1);
        totalRateNum = 0;
        for (int i = 0; i < rateArray.length; i++) {
            totalRateNum += rateArray[i];
            if (randNum < totalRateNum) {
                return i;
            }
        }
        return 0;
    }

    public static int total(int[] rateArray) {
        int totalRateNum = 0;
        for (int i = 0; i < rateArray.length; i++) {
            int rateNum = rateArray[i];
            totalRateNum += rateNum;
        }
        return totalRateNum;
    }


    /**
     * 根据属性类型随机属性值
     * @param propType 属性类型
     * @param minVal 最小随机值
     * @param maxVal 最大随机值
     * @return minVal-maxVal 随机数
     */
//	public static float getRandomPropertyVal(PropertyType propType,float minVal, float maxVal) {
//		if (maxVal < minVal) {
//			return 0f;
//		}
//		if (PropertyType.CRITICAL_MULTIPLE == propType
//				|| PropertyType.CRITICAL_BREAK_MULTIPLE == propType) {
//			return MathUtil.getRandomFloat(minVal, maxVal);
//		} else {
//			return MathUtil.getRandomInt((int)minVal, (int)maxVal);
//		}
//	}

    /**
     * 格式化随机后的属性值
     *
     * @param val 值
     * @return 格式化后的字符串
     */
    public static String formatPropertyVal(double val) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(val);
    }

}
