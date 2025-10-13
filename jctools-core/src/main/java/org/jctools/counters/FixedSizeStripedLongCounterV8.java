package org.jctools.counters;

import org.jctools.util.UnsafeLongArrayAccess;

/**
 * Wait-free implementation of striped counter using
 * Java 8 Unsafe intrinsics (lock addq and lock xchg).
 *
 * @author Tolstopyatov Vsevolod
 */
class FixedSizeStripedLongCounterV8 extends FixedSizeStripedLongCounter {

    public FixedSizeStripedLongCounterV8(int stripesCount) {
        super(stripesCount);
    }

    @Override
    protected void inc(long[] cells, long offset, long delta) {
        UnsafeLongArrayAccess.getAndAddLongElement(cells, offset, delta);
    }

    @Override
    protected long getAndReset(long[] cells, long offset) {
        return UnsafeLongArrayAccess.getAndSetLongElement(cells, offset, 0L);
    }
}
