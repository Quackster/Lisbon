package net.h4bbo.lisbon.crypto;

/**
 * Decodes the obfuscated SECRETKEY payload used by the Director client when it
 * enables server->client encryption.
 */
public final class SecretKeyCodec {

    /**
     * Creates a new SecretKeyCodec.
     */
    private SecretKeyCodec() {}

    /**
     * Secrets decode.
     * @param encodedKey the encoded key value
     * @return the result of this operation
     */
    public static int secretDecode(String encodedKey) {
        if (encodedKey == null || encodedKey.isEmpty()) {
            return 0;
        }

        int length = encodedKey.length();
        if ((length % 2) != 0) {
            length--;
        }

        String table = encodedKey.substring(0, encodedKey.length() / 2);
        String key = encodedKey.substring(encodedKey.length() / 2, length);
        int checksum = 0;

        for (int index = 0; index < key.length(); index++) {
            char value = key.charAt(index);
            int offset = table.indexOf(value);
            int decoded = offset >= 0 ? offset : -1;

            if ((decoded % 2) == 0) {
                decoded *= 2;
            }
            if ((index % 3) == 0) {
                decoded *= 3;
            }
            if (decoded < 0) {
                decoded = key.length() % 2;
            }

            checksum += decoded;
            checksum ^= decoded << ((index % 3) * 8);
        }

        return checksum;
    }
}
