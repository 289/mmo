/**
 */
package mmo.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author panqingqing
 * @since 2012-3-23 ����05:21:30
 */
public abstract class OperatorSequence {

    private static final AtomicInteger sequence = new AtomicInteger(0);

    public static int getAndIncrement() {
        return sequence.getAndIncrement();
    }

}
