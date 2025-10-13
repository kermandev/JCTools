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
    public static <E> void spRefElement(E[] buffer, long offset, E e)
    {
        assert (int) offset == offset: "mismatch while migrating";
        OBJECT_A.set(buffer, (int) offset, e);
    }

    /**
     * An ordered store of an element to a given offset
     *
     * @param buffer this.buffer
     * @param offset computed via {@link UnsafeRefArrayAccess#calcCircularRefElementOffset}
     * @param e      an orderly kitty
     */
    public static <E> void soRefElement(E[] buffer, long offset, E e)
    {
        assert (int) offset == offset: "mismatch while migrating";
        OBJECT_A.setRelease(buffer, (int) offset, e);
    }

    /**
     * A plain load (no ordering/fences) of an element from a given offset.
     *
     * @param buffer this.buffer
     * @param offset computed via {@link UnsafeRefArrayAccess#calcRefElementOffset(long)}
     * @return the element at the offset
     */
    @SuppressWarnings("unchecked")
    public static <E> E lpRefElement(E[] buffer, long offset)
    {
        assert (int) offset == offset: "mismatch while migrating";
        return (E) OBJECT_A.get(buffer, (int) offset);
    }

    /**
     * A volatile load of an element from a given offset.
     *
     * @param buffer this.buffer
     * @param offset computed via {@link UnsafeRefArrayAccess#calcRefElementOffset(long)}
     * @return the element at the offset
     */
    @SuppressWarnings("unchecked")
    public static <E> E lvRefElement(E[] buffer, long offset)
    {
        assert (int) offset == offset: "mismatch while migrating";
        return (E) OBJECT_A.getVolatile(buffer, (int) offset);
    }


    /**
     * A compare and set of element
     * @param buffer this.buffer
     * @param offset computed via {@link UnsafeRefArrayAccess#calcRefElementOffset(long)}
     * @param expectedValue the old
     * @param newValue the new
     * @return true if successful
     */
    public static <E> boolean casRefElement(E[] buffer, long offset, E expectedValue, E newValue) {
        assert (int) offset == offset: "mismatch while migrating";
        return OBJECT_A.compareAndSet(buffer, (int) offset, expectedValue, newValue);
    }

    /**
     * @param index desirable element index
     * @return the offset in bytes within the array for a given index
     */
    public static int calcRefElementOffset(long index)
    {
        assert (int) index == index: "mismatch while migrating";
        return (int) index;
    }

    /**
     * Note: circular arrays are assumed a power of 2 in length and the `mask` is (length - 1).
     *
     * @param index desirable element index
     * @param mask (length - 1)
     * @return the offset in bytes within the circular array for a given index
     */
    public static long calcCircularRefElementOffset(long index, long mask)
    {
        assert (int) index == index: "mismatch while migrating";
        return (index & mask);
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
