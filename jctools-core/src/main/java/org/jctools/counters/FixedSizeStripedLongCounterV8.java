package org.jctools.counters;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * Wait-free implementation of striped counter using
 * Java 8 Unsafe intrinsics (lock addq and lock xchg).
 *
 * @author Tolstopyatov Vsevolod
 */
class FixedSizeStripedLongCounterV8 extends FixedSizeStripedLongCounter {
    private final static VarHandle LONG_A = MethodHandles.arrayElementVarHandle(long[].class);

    public FixedSizeStripedLongCounterV8(int stripesCount) {
        super(stripesCount);
    }

    @Override
    protected void inc(long[] cells, long offset, long delta) {
        LONG_A.getAndAdd(cells, offset, delta);
    }

    @Override
    protected long getAndReset(long[] cells, long offset) {
        return (long) LONG_A.getAndSet(cells, offset, 0L);
    }
}
