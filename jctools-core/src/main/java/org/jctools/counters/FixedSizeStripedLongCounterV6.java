package org.jctools.counters;

import org.jctools.util.UnsafeLongArrayAccess;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

/**
 * Lock-free implementation of striped counter using
 * CAS primitives.
 *
 * @author Tolstopyatov Vsevolod
 */
class FixedSizeStripedLongCounterV6 extends FixedSizeStripedLongCounter {

    public FixedSizeStripedLongCounterV6(int stripesCount) {
        super(stripesCount);
    }

    @Override
    protected void inc(long[] cells, long offset, long delta) {
        long v;
        do {
            v = (long) UnsafeLongArrayAccess.lvLongElement(cells, offset);
        } while (!UnsafeLongArrayAccess.casRefElement(cells, offset, v, v + delta));
    }

    @Override
    protected long getAndReset(long[] cells, long offset) {
        long v;
        do {
            v = (long) UnsafeLongArrayAccess.lvLongElement(cells, offset);
        } while (!UnsafeLongArrayAccess.casRefElement(cells, offset, v, 0L));

        return v;
    }
}
