/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jctools.util;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

@InternalAPI
public final class UnsafeLongArrayAccess
{
    private static final VarHandle LONG_A = MethodHandles.arrayElementVarHandle(long[].class);

    /**
     * A plain store (no ordering/fences) of an element to a given offset
     *
     * @param buffer le buffer
     * @param offset computed via {@link UnsafeLongArrayAccess#calcLongElementOffset(long)}
     * @param e      an orderly kitty
     */
    public static void spLongElement(long[] buffer, long offset, long e)
    {
        LONG_A.set(buffer, offset, e);
    }

    /**
     * An ordered store of an element to a given offset
     *
     * @param buffer le buffer
     * @param offset computed via {@link UnsafeLongArrayAccess#calcCircularLongElementOffset}
     * @param e      an orderly kitty
     */
    public static void soLongElement(long[] buffer, long offset, long e)
    {
        LONG_A.setRelease(buffer, offset, e);
    }

    /**
     * A plain load (no ordering/fences) of an element from a given offset.
     *
     * @param buffer le buffer
     * @param offset computed via {@link UnsafeLongArrayAccess#calcLongElementOffset(long)}
     * @return the element at the offset
     */
    public static long lpLongElement(long[] buffer, long offset)
    {
        return (long) LONG_A.get(buffer, offset);
    }

    /**
     * A volatile load of an element from a given offset.
     *
     * @param buffer le buffer
     * @param offset computed via {@link UnsafeLongArrayAccess#calcCircularLongElementOffset}
     * @return the element at the offset
     */
    public static long lvLongElement(long[] buffer, long offset)
    {
        return (long) LONG_A.getVolatile(buffer, offset);
    }

    /**
     * @param index desirable element index
     * @return the offset in bytes within the array for a given index
     */
    public static long calcLongElementOffset(long index)
    {
        return index;
    }

    /**
     * Note: circular arrays are assumed a power of 2 in length and the `mask` is (length - 1).
     *
     * @param index desirable element index
     * @param mask (length - 1)
     * @return the offset in bytes within the circular array for a given index
     */
    public static long calcCircularLongElementOffset(long index, long mask) {
        return (index & mask);
    }

    /**
     * This makes for an easier time generating the atomic queues, and removes some warnings.
     */
    public static long[] allocateLongArray(int capacity)
    {
        return new long[capacity];
    }
}
