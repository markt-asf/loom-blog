package org.apache.markt;

import java.nio.charset.StandardCharsets;

public enum FixedSizeResponse {

    EXP_8(8),   //  256 bytes
    EXP_10(10), //   1k bytes
    EXP_12(12), //   4k bytes
    EXP_14(14), //  16k bytes
    EXP_16(16), //  64k bytes
    EXP_18(18), // 256k bytes
    EXP_20(20), //   1M bytes
    EXP_22(22), //   4M bytes
    EXP_24(24); //  16M bytes

    private final byte[] data;

    private FixedSizeResponse(int exp) {
        int size = 1 << exp;
        int counterLimit = size / 8;

        data = new byte[size];

        for (int counter = 0; counter < counterLimit; counter++) {
            String value = String.format("%08x", Integer.valueOf(counter));
            System.arraycopy(value.getBytes(StandardCharsets.ISO_8859_1), 0, data, counter * 8, 8);
        }

    }

    public byte[] getData() {
        return data;
    }
}
