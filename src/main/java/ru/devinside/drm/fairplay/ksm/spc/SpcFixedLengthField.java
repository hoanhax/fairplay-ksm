package ru.devinside.drm.fairplay.ksm.spc;

public enum SpcFixedLengthField {
    VERSION,
    RESERVED,

    // SPC data initialization vector. A CBC initialization vector that has a unique value for each SPC message.
    SPC_DATA_IV,

    // Encrypted AES-128 key. The key for decrypting the SPC payload.
    // This key is itself encrypted, using RSA public key encryption with Optimal Asymmetric Encryption Padding.
    ENCRYPTED_SPC_KEY,

    // The SHA-1 hash value of the encrypted Application Certificate,
    // which identifies the private key of the application that generated the SPC.
    CERTIFICATE_HASH;

    private int size;

    static {
        VERSION.size = 4;
        RESERVED.size = 4;
        SPC_DATA_IV.size = 16;
        ENCRYPTED_SPC_KEY.size = 128;
        CERTIFICATE_HASH.size = 20;
    }

    public int getSize() {
        return size;
    }
}