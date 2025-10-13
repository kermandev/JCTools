package org.jctools.queues;

import org.jctools.util.InternalAPI;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

import static org.jctools.util.UnsafeRefArrayAccess.*;

@InternalAPI
public class MpUnboundedXaddChunk<R, E> {
    public final static int NOT_USED = -1;

    private final static VarHandle PREV_OFFSET;
    private final static VarHandle NEXT_OFFSET;
    private final static VarHandle INDEX_OFFSET;

    static {
        try {
            PREV_OFFSET = MethodHandles.lookup().findVarHandle(MpUnboundedXaddChunk.class, "prev", Object.class);
            NEXT_OFFSET = MethodHandles.lookup().findVarHandle(MpUnboundedXaddChunk.class, "next", Object.class);
            INDEX_OFFSET = MethodHandles.lookup().findVarHandle(MpUnboundedXaddChunk.class, "index", long.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final boolean pooled;
    private final E[] buffer;

    private volatile R prev;
    private volatile long index;
    private volatile R next;
    protected MpUnboundedXaddChunk(long index, R prev, int size, boolean pooled)
    {
        buffer = allocateRefArray(size);
        // next is null
        soPrev(prev);
        spIndex(index);
        this.pooled = pooled;
    }

    public final boolean isPooled()
    {
        return pooled;
    }

    public final long lvIndex()
    {
        return index;
    }

    public final void soIndex(long index)
    {
        INDEX_OFFSET.setRelease(this, index);
    }

    final void spIndex(long index)
    {
        INDEX_OFFSET.set(this, index);
    }

    public final R lvNext()
    {
        return next;
    }

    public final void soNext(R value)
    {
        NEXT_OFFSET.setRelease(this, value);
    }

    public final R lvPrev()
    {
        return prev;
    }

    public final void soPrev(R value)
    {
        PREV_OFFSET.set(this, value);
    }

    public final void soElement(int index, E e)
    {
        soRefElement(buffer, calcRefElementOffset(index), e);
    }

    public final E lvElement(int index)
    {
        return lvRefElement(buffer, calcRefElementOffset(index));
    }

    public final E spinForElement(int index, boolean isNull)
    {
        E[] buffer = this.buffer;
        long offset = calcRefElementOffset(index);
        E e;
        do
        {
            e = lvRefElement(buffer, offset);
        }
        while (isNull != (e == null));
        return e;
    }
}
