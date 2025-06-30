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

import com.reandroid.arsc.base.Block;
import com.reandroid.arsc.io.BlockReader;
import com.reandroid.arsc.item.LongReference;

import java.io.IOException;
import java.io.InputStream;

public class MetadataInteger extends VersionSpecificItem implements LongReference {

    private final VersionRange compressedVersion;
    private final boolean signed;
    private long mValue;

    public MetadataInteger(VersionRange versionRange, VersionRange compressedVersion, boolean signed) {
        super(versionRange, 4);
        this.compressedVersion = compressedVersion;
        this.signed = signed;
    }
    public MetadataInteger(VersionRange versionRange, boolean signed) {
        this(versionRange, COMPRESSED_VERSION, signed);
    }
    public MetadataInteger(boolean signed) {
        this(null, COMPRESSED_VERSION, signed);
    }

    public boolean isSigned() {
        return signed;
    }
    public boolean isCompressed() {
        VersionRange versionRange = this.getCompressedVersion();
        if (versionRange == null) {
            return true;
        }
        return versionRange.contains(getFileVersion());
    }

    public VersionRange getCompressedVersion() {
        return compressedVersion;
    }

    public static final VersionRange COMPRESSED_VERSION = new VersionRange(27.1, null);

    @Override
    public long getLong() {
        return mValue;
    }

    @Override
    public void set(long l) {
        if (l != mValue) {
            encodeValue((int) l);
        }
    }

    @Override
    public int get() {
        return (int) getLong();
    }

    @Override
    public void set(int i) {
        long l;
        if (isSigned()) {
            l = i;
        } else {
            l = i & 0xffffffffL;
        }
        set(l);
    }

    @Override
    public void onReadBytes(BlockReader reader) throws IOException {
        if (!hasValidVersion()) {
            return;
        }
        boolean is = isNull();
        if (is || isNull()) {
            return;
        }
        if (isCompressed()) {
            int[] read = readCompressedUInt32(reader);
            int length = read[0];
            int value = read[1];
            reader.seek(reader.getPosition() - length);
            reader.readFully(initBytes(length));
            mValue = convertToSigned(value);
        } else {
            setBytesLength(4, false);
            byte[] bytes = getBytesInternal();
            reader.readFully(bytes);
            long value = Block.getInteger(bytes, 0);
            if (!isSigned()) {
                value = value & 0xffffffffL;
            }
            mValue = value;
        }
    }

    @Override
    public int readBytes(InputStream inputStream) throws IOException {
        if (!hasValidVersion()) {
            return 0;
        }
        if (isNull()) {
            return 0;
        }
        int length;
        if (isCompressed()) {
            int[] read = readCompressedUInt32(inputStream);
            length = read[0];
            int value = read[1];
            int encodedLength = writeCompressedUInt32(initBytes(length), value);
            if (encodedLength != length) {
                throw new IOException("Mismatch in read length = " + length
                        + " and encoded = " + encodedLength);
            }
            mValue = convertToSigned(value);
            return length;
        } else {
            length = 4;
            setBytesLength(length, false);
            byte[] bytes = getBytesInternal();
            length = inputStream.read(bytes, 0, length);
            long value = Block.getInteger(bytes, 0);
            if (!isSigned()) {
                value = value & 0xffffffffL;
            }
            mValue = value;
        }
        return length;
    }

    protected void encodeValue(int value) {
        if (isCompressed()) {
            value = convertToUnsigned(value);
            byte[] bytes = initBytes(computeCompressedUInt32Size(value));
            writeCompressedUInt32(bytes, value);
        } else {
            setBytesLength(4, false);
            Block.putInteger(getBytesInternal(), 0, value);
        }
    }
    protected byte[] initBytes(int length) {
        setBytesLength(length, false);
        return getBytesInternal();
    }
    private int convertToSigned(int unsigned) {
        if (isSigned()) {
            if (unsigned == 0xFFFFFFFF) {
                return Integer.MIN_VALUE;
            }
            boolean isNegative = (unsigned & 1) != 0;
            unsigned >>>= 1;
            return isNegative ? -unsigned - 1 : unsigned;
        }
        return unsigned;
    }
    private int convertToUnsigned(int signed) {
        if (isSigned()) {
            int encoded;
            if (signed == Integer.MIN_VALUE) {
                encoded = 0xFF;
            } else {
                boolean isNegative = signed < 0;
                encoded = isNegative ? (-(signed + 1)) : signed;
                encoded <<= 1;
                if (isNegative) {
                    encoded |= 1;
                }
            }
            return encoded;
        }
        return signed;
    }

    @Override
    public String toString() {
        return Long.toString(getLong());
    }

    public static int[] readCompressedUInt32(InputStream inputStream) throws IOException {
        int length;
        int value;
        int read = inputStream.read();
        if ((read & 0x80) == 0) {
            // 1 byte written
            value = read;
            length = 1;
        } else if ((read & 0xC0) == 0x80) {
            // 2 bytes written
            value = (read & ~0x80) << 8;
            value |= inputStream.read();
            length = 2;
        } else if ((read & 0xE0) == 0xC0) {
            // 4 bytes written
            value = (read & ~0xC0) << 24;
            value |= (inputStream.read() << 16);
            value |= (inputStream.read() << 8);
            value |= inputStream.read();
            length = 4;
        } else if (read == 0xF0) {
            value = inputStream.read() << 24;
            value |= (inputStream.read() << 16);
            value |= (inputStream.read() << 8);
            value |= inputStream.read();
            length = 5;
        } else if (read == 0xFE) {
            // Special encoding for Int32.MaxValue
            value = 0xfffffffe;
            length = 1;
        } else if (read == 0xFF) {
            // Yes we treat UInt32.MaxValue (and Int32.MinValue, see ReadCompressedInt32) specially
            value = 0xffffffff;
            length = 1;
        } else {
            throw new IOException("Invalid compressed integer format: " + read + ", " + inputStream.read());
        }
        return new int[]{length, value};
    }
    public static int writeCompressedUInt32(byte[] bytes, int value) {
        if (value == 0xFFFFFFFF) {
            bytes[0] = (byte)0xFF;
            return 1;
        } else if (value == 0xFFFFFFFE) {
            bytes[0] = (byte)0xFE;
            return 1;
        } else if (value <= 0x7F) {
            bytes[0] = (byte)value;
            return 1;
        } else if (value <= 0x3FFF) {
            bytes[0] = (byte)(0x80 | (value >> 8));
            bytes[1] = (byte)(value & 0xFF);
            return 2;
        } else if (value <= 0x1FFFFFFF) {
            bytes[0] = (byte)(0xC0 | (value >> 24));
            bytes[1] = (byte)((value >> 16) & 0xFF);
            bytes[2] = (byte)((value >> 8) & 0xFF);
            bytes[3] = (byte)(value & 0xFF);
            return 4;
        } else {
            bytes[0] = (byte)0xF0;
            bytes[1] = (byte)((value >> 24) & 0xFF);
            bytes[2] = (byte)((value >> 16) & 0xFF);
            bytes[3] = (byte)((value >> 8) & 0xFF);
            bytes[4] = (byte)(value & 0xFF);
            return 5;
        }
    }
    private static int computeCompressedUInt32Size(int value) {
        if (value <= 0x7F) {
            return 1;
        } else if (value <= 0x3FFF) {
            return 2;
        } else if (value <= 0x1FFFFFFF) {
            return 4;
        } else {
            return 5;
        }
    }
}
