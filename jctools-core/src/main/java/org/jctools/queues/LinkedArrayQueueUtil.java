package org.jctools.queues;

import org.jctools.util.InternalAPI;

/**
 * This is used for method substitution in the LinkedArray classes code generation.
 */
@InternalAPI
public final class LinkedArrayQueueUtil
{
    public static int length(Object[] buf)
    {
        return buf.length;
    }

    /**
     * This method assumes index is actually (index << 1) because lower bit is
     * used for resize. This is compensated for by reducing the element shift.
     * The computation is constant folded, so there's no cost.
     */
    public static long modifiedCalcCircularRefElementOffset(long index, long mask)
    {
        return (index & mask) << 1;
    }

    public static long nextArrayOffset(Object[] curr)
    {
        return length(curr) - 1;
    }
}
