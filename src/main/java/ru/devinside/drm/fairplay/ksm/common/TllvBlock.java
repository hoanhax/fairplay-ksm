package ru.devinside.drm.fairplay.ksm.common;

import org.apache.commons.lang3.RandomUtils;
import ru.devinside.drm.fairplay.ksm.spc.SpcTag;
import ru.devinside.util.Hexler;

import java.nio.ByteBuffer;

/**
 * Tag-length-length-value block
 */
public class TllvBlock {
    // A sequence of bytes that is unique within an SPC/CKC
    private final long tag;

    // The payload of the TLLV, starting with byte 16 of the block.
    private final byte[] value;

    // The number of bytes in the value plus padding fields of the TLLV (following the tag, block length,
    // and value length fields). The block length must be filled out to a multiple of 16 bytes by extending
    // the padding field.
    private final int totalBlockLength;

    // A field that begins with the next byte after the value field (byte k +1).
    // It must fill out the TLLV to a multiple of 16 bytes, but may be randomly extended in increments of
    // 16 bytes. Thus the following relation holds: padding_size = block_length - value_length
    // The padding field must contain random values, not all 0x00 or 0xFF bytes.
    private final byte[] padding;

    public TllvBlock(long tag, byte[] value, int totalBlockLength, byte[] padding) {
        this.tag = tag;
        this.value = value;
        this.totalBlockLength = totalBlockLength;
        this.padding = padding;
    }

    public TllvBlock(long tag, byte[] value) {
        this.tag = tag;
        this.value = value;

        int paddingSize = 32 - value.length % 16; // Extend to nearest 16 bytes + extra 16 bytes
        this.totalBlockLength = value.length + paddingSize;
        this.padding = RandomUtils.nextBytes(paddingSize);
    }

    public long getTag() {
        return tag;
    }

    public byte[] getValue() {
        return value;
    }

    public int length() {
        return Long.BYTES + Integer.BYTES + Integer.BYTES + value.length + padding.length;
    }

    private int getTotalBlockLength() {
        return totalBlockLength;
    }

    public byte[] asBytes() {
        ByteBuffer out = ByteBuffer.allocate(length());
        out.putLong(this.getTag());
        out.putInt(this.getTotalBlockLength());
        out.putInt(this.getValue().length);
        out.put(value);
        out.put(padding);
        return out.array();
    }

    @Override
    public String toString() {
        return "TllvBlock{" +
                "tag=" + Long.toHexString(tag) + " " + SpcTag.valueOf(tag) +
                ", value=" + Hexler.toHexString(value) +
                ", valueLength=" + value.length +
                ", totalBlockLength=" + totalBlockLength +
                ", paddingLength=" + padding.length +
                '}';
    }
}
