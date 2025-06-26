/*
 *  Copyright (C) 2022 github.com/REAndroid
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reandroid.unity.metadata.base;

import java.io.IOException;

public class MDCompressedSInt32 extends MDCompressedInt32 {

    public MDCompressedSInt32(VersionRange versionRange) {
        super(versionRange);
    }
    public MDCompressedSInt32() {
        super();
    }

    @Override
    public final boolean isSigned() {
        return true;
    }

    // For the following java class, complete "readChar" method
    // Do not use any charset encoder, use only "read()" method
    // I don't want your explanation, description or comments just print only your "readChar()"

    public abstract class DataReader {

        // Reads unsigned one byte (Similar to java.io.InputStream#read)
        public abstract int read() throws IOException;

        // Read one utf-8 character
        public char readChar() throws IOException {
            int firstByte = this.read();
            if ((firstByte & 0xFFFFFF80) == 0) {
                return (char) firstByte;
            } else if ((firstByte & 0xE0) == 0xC0) {
                int secondByte = this.read();
                return (char) (((firstByte & 0x1F) << 6) | (secondByte & 0x3F));
            } else if ((firstByte & 0xF0) == 0xE0) {
                int secondByte = this.read();
                int thirdByte = this.read();
                return (char) (((firstByte & 0x0F) << 12) | ((secondByte & 0x3F) << 6) | (thirdByte & 0x3F));
            }
            throw new IOException("Invalid UTF-8 character");
        }
        public int computeUtf8BytesLength(char c) {
            if ((c & 0xFFFFFF80) == 0) {
                return 1;
            } else if ((c & 0xF800) == 0) {
                return 2;
            } else {
                return 3;
            }
        }

        public int writeUtf8Bytes(byte[] bytes, char c) {
            if ((c & 0xFFFFFF80) == 0) {
                bytes[0] = (byte) c;
                return 1;
            } else if ((c & 0xF800) == 0) {
                bytes[0] = (byte) (0xC0 | (c >> 6));
                bytes[1] = (byte) (0x80 | (c & 0x3F));
                return 2;
            } else {
                bytes[0] = (byte) (0xE0 | (c >> 12));
                bytes[1] = (byte) (0x80 | ((c >> 6) & 0x3F));
                bytes[2] = (byte) (0x80 | (c & 0x3F));
                return 3;
            }
        }
    }
}
