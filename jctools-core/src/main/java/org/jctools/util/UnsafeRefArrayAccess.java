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
public final class UnsafeRefArrayAccess
{
    private final static VarHandle OBJECT_A = MethodHandles.arrayElementVarHandle(Object[].class);

    /**
     * A plain store (no ordering/fences) of an element to a given offset
     *
     * @param buffer this.buffer
     * @param offset computed via {@link UnsafeRefArrayAccess#calcRefElementOffset(long)}
     * @param e      an orderly kitty
     */
    public static <E> void spRefElement(E[] buffer, int offset, E e)
    {
        OBJECT_A.set(buffer, offset, e);
    }

    /**
     * An ordered store of an element to a given offset
     *
     * @param buffer this.buffer
     * @param offset computed via {@link UnsafeRefArrayAccess#calcCircularRefElementOffset}
     * @param e      an orderly kitty
     */
    public static <E> void soRefElement(E[] buffer, int offset, E e)
    {
        OBJECT_A.setRelease(buffer, offset, e);
    }

    /**
     * A plain load (no ordering/fences) of an element from a given offset.
     *
     * @param buffer this.buffer
     * @param offset computed via {@link UnsafeRefArrayAccess#calcRefElementOffset(long)}
     * @return the element at the offset
     */
    @SuppressWarnings("unchecked")
    public static <E> E lpRefElement(E[] buffer, int offset)
    {
        return (E) OBJECT_A.get(buffer, offset);
    }

    /**
     * A volatile load of an element from a given offset.
     *
     * @param buffer this.buffer
     * @param offset computed via {@link UnsafeRefArrayAccess#calcRefElementOffset(long)}
     * @return the element at the offset
     */
    @SuppressWarnings("unchecked")
    public static <E> E lvRefElement(E[] buffer, int offset)
    {
        return (E) OBJECT_A.getVolatile(buffer, offset);
    }

    /**
     * @param index desirable element index
     * @return the offset in bytes within the array for a given index
     */
    public static int calcRefElementOffset(long index)
    {
        return (int) index;
    }

    /**
     * Note: circular arrays are assumed a power of 2 in length and the `mask` is (length - 1).
     *
     * @param index desirable element index
     * @param mask (length - 1)
     * @return the offset in bytes within the circular array for a given index
     */
    public static int calcCircularRefElementOffset(long index, long mask)
    {
        return (int) (index & mask);
    }

    /**
     * This makes for an easier time generating the atomic queues, and removes some warnings.
     */
    @SuppressWarnings("unchecked")
    public static <E> E[] allocateRefArray(int capacity)
    {
        return (E[]) new Object[capacity];
    }
}
