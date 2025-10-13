package org.jctools.counters;

/**
 * @author Tolstopyatov Vsevolod
 */
public final class CountersFactory {

    private CountersFactory() {
    }

    public static FixedSizeStripedLongCounter createFixedSizeStripedCounter(int stripesCount) {
        return new FixedSizeStripedLongCounterV8(stripesCount);
    }

    public static FixedSizeStripedLongCounter createFixedSizeStripedCounterV6(int stripesCount) {
        return new FixedSizeStripedLongCounterV6(stripesCount);
    }

    public static FixedSizeStripedLongCounter createFixedSizeStripedCounterV8(int stripesCount) {
        return new FixedSizeStripedLongCounterV8(stripesCount);
    }
}
