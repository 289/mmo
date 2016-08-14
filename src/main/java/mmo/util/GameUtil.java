package mmo.util;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 游戏内的算法
 */
public class GameUtil {

    /**
     * 自动生成组队ID的类
     */
    private static AtomicInteger atomicInteger = new AtomicInteger();

    /**
     * 概率的极值
     */
    public final static int MAX_RATE = 10000;

    public static final int random(int from, int to) {
        if (from >= to)
            return from;

        return MathUtil.getRandom().nextInt(to - from) + from;
    }

    /**
     * 将指定概率中随机选择
     *
     * @param rates
     * @return -1表示无选择
     */
    public static int pickResult(int[] rates) {
        if (rates == null || rates.length == 0)
            return -1;
        // 累加排列
        int sum = 0;
        for (int index = 0; index < rates.length; index++) {
            int rate = clamp(rates[index]);
            rates[index] = sum + rate;
            sum += rate;
        }

        // 获取[1, 10000]之间的随机数
        int temp = GameUtil.random(1, MAX_RATE + 1);
        String rateStr = "";
        for (int index = 0; index < rates.length; index++) {
            rateStr += "\t" + rates[index];
            if (temp <= rates[index])
                return index;
        }
        return -1;
    }

    /**
     * 将指定值限制于[0,10000]范围内
     *
     * @param value
     * @return
     */
    private static int clamp(int value) {
        if (value < 0)
            value = 0;
        else if (value > MAX_RATE)
            value = MAX_RATE;
        return value;
    }

    /**
     * 工具方法，通过每页的数量和页返回List
     *
     * @param <T>
     * @param list        源List
     * @param pageRecords 每页数量，大于0
     * @param page        页码，大于等于0
     * @return
     */
    public static <T> List<T> getPageForList(List<T> list, int pageRecords, int page) {
        if (list == null || list.size() == 0)
            return null;
        if (pageRecords < 1)
            return null;
        // List里对象总数
        int totalRows = list.size();

        // 开始位置
        int pageStartRow;
        // 结束位置
        int pageEndRow;
        // 总的页数
        int totalPages;

        if ((totalRows % pageRecords) == 0) {
            totalPages = totalRows / pageRecords - 1;
        } else {
            totalPages = totalRows / pageRecords;
        }

        // 不是最后一页
        if (page < totalPages) {
            pageEndRow = (page + 1) * pageRecords;
        }
        // 最后一页
        else {
            pageEndRow = totalRows;
        }
        pageStartRow = page * pageRecords;
        return list.subList(pageStartRow, pageEndRow);
    }


    /**
     * 自动生成组队ID的方法
     *
     * @return
     */
    public static int autoCreateTeamId() {
        return atomicInteger.incrementAndGet();
    }

    public static int getTimeInterval(long srcTime, long destTime) {
        long day = 3600 * 1000 * 24;
        int timeDay = (int) (destTime / day - srcTime / day);
        return timeDay;
    }

    /**
     * 以x,y 为原点 radius 为半径 在周围 生成一圈点
     *
     * @param x
     * @param y
     * @param radius
     * @return
     */
    public static short[] createCirclePoint(short x, short y, int radius) {
        return null;
    }



}
