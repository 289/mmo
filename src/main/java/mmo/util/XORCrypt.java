package mmo.util;

/**
 * 异或加密
 * 原理 a ^ b = c  ——>  a = b ^ c
 * 客户端服务器使用同一份key，包的加密与解密要保持顺序，不然会导致无法解密数据
 *
 * @author Jin Shuai
 */
public class XORCrypt {

    /**
     * 解密方法
     * 解密之后key会变化，所以可以防止封包
     *
     * @param data       原始数据
     * @param decryptKey 解密key 64位
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, int[] decryptKey) throws Exception {
        if (data.length == 0) {
            return data;
        }

        if (decryptKey.length < 8) {
            throw new Exception("The decryptKey must be 64bits length!");
        }

        int length = data.length;
        int lastCipherByte;
        int plainText;
        int key;

        // 解密首字节
        lastCipherByte = data[0] & 0xff;
        data[0] ^= decryptKey[0];

        for (int index = 1; index < length; index++) {
            // 解密当前字节
            key = ((decryptKey[index & 0x7] + lastCipherByte) ^ index);
            plainText = (((data[index] & 0xff) - lastCipherByte) ^ key) & 0xff;

            // 更新变量值
            lastCipherByte = data[index] & 0xff;
            data[index] = (byte) plainText;
            decryptKey[index & 0x7] = (byte) (key & 0xff);
        }

        return data;
    }

    /**
     * 加密方法
     * 解密后key会变化
     *
     * @param data       原始数据
     * @param encryptKey 加密key,64位
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, int[] encryptKey) throws Exception {
        if (data.length == 0) {
            return data;
        }

        if (encryptKey.length < 8) {
            throw new Exception("The encryptKey must be 64bits length!");
        }

        int lastCipherByte = 0;

        byte[] plainText = data;
        int length = plainText.length;

        // 加密首字节
        lastCipherByte = (byte) ((plainText[0] ^ encryptKey[0]) & 0xff);
        plainText[0] = (byte) lastCipherByte;

        // 循环加密
        int keyIndex = 0;
        for (int i = 1; i < length; i++) {
            keyIndex = i & 0x7;
            encryptKey[keyIndex] = ((encryptKey[keyIndex] + lastCipherByte) ^ i) & 0xff;
            lastCipherByte = (((plainText[i] ^ encryptKey[keyIndex]) & 0xff) + lastCipherByte) & 0xff;
            plainText[i] = (byte) lastCipherByte;
        }
        return plainText;
    }

}
