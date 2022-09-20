/* The credit for this code goes to ianm1647 of farmersdelight and the original can be found below:
 * https://github.com/ianm1647/farmersdelight/blob/master/src/main/java/com/nhoryzon/mc/farmersdelight/exception/SlotInvalidRangeException.java
 * This piece of code is under the MIT License:
 * MIT License
 *
 * Copyright (c) 2020 vectorwing, Zifiv
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package timefall.goodtea.exceptions;

public class SlotInvalidRangeException extends RuntimeException {

    public SlotInvalidRangeException(int slotNumber, int maxRange) {
        super("Slot " + slotNumber + " not in valid range - [0," + maxRange + ")");
    }

}
