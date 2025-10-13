package org.jctools.counters;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * Lock-free implementation of striped counter using
 * CAS primitives.
 *
 * @author Tolstopyatov Vsevolod
 */
class FixedSizeStripedLongCounterV6 extends FixedSizeStripedLongCounter {
    private final static VarHandle LONG_A = MethodHandles.arrayElementVarHandle(long[].class);

    public FixedSizeStripedLongCounterV6(int stripesCount) {
        super(stripesCount);
    }

    @Override
    protected void inc(long[] cells, long offset, long delta) {
        long v;
        do {
            v = (long) LONG_A.getVolatile(cells, offset);
        } while (!LONG_A.compareAndSet(cells, offset, v, v + delta));
    }

    @Override
    protected long getAndReset(long[] cells, long offset) {
        long v;
        do {
            v = (long) LONG_A.getVolatile(cells, offset);
        } while (!LONG_A.compareAndSet(cells, offset, v, 0L));

        return v;
    }
}
