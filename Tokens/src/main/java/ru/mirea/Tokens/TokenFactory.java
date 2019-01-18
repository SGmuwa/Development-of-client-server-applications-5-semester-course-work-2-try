package ru.mirea.Tokens;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Класс представляет из себя описание, что такое токен.
 */
public class TokenFactory {
    public final static HeaderToken headerDefault = new HeaderToken("PupishevaSidorenko", "SHA-512");
    /**
     * Секретный ключ.
     */
    public final static String secretKey = "4QdR!t*NBEXDzWf_M=G%f$eA4ku?ZTdVDNarxH7z4Q=mvg8ZXL";

    /**
     * Генерирует токен на основе его полезной нагрузки.
     * @param payload Полезная нагрузка токена.
     * @return Токен пользователя.
     */
    public static String generateToken(PayloadToken payload) {
        String header64 = new String(Base64.getEncoder().encode(headerDefault.generateJSON().getBytes()));
        String payload64 = new String(Base64.getEncoder().encode(payload.generateJSON().getBytes()));
        return header64 + "." + payload64 + "." + getSignature(header64, payload64);
    }

    /**
     * Получает информацию о пользователе по его токену.
     * @param token Токен пользователя.
     * @return Данные пользователя. Null, если ошибка.
     */
    public static PayloadToken decoderToken(String token) {
        String[] tokenArray = token.split("\\.");
        String headerSTR = new String(Base64.getDecoder().decode(tokenArray[0]));
        String payloadSTR = new String(Base64.getDecoder().decode(tokenArray[1]));
        String signature = tokenArray[2];
        HeaderToken header;
        PayloadToken payload;
        try {
            header = HeaderToken.decoderJson(headerSTR);
            payload = PayloadToken.decoderJson(payloadSTR);
        } catch (Exception e) {
            return null;
        }
        if(!headerDefault.equals(header))
            return null;
        if(!getSignature(tokenArray[0], tokenArray[1]).equals(signature))
            // Подпись не совпала.
            return null;
        return payload;
    }

    /**
     * Генерация подписи.
     * @param header64 Заголовок токена в формате base64.
     * @param payload64 Информация о пользователе в формате base64.
     * @return Получение подписи токена.
     */
    private static String getSignature(String header64, String payload64) {
        /*
        StringBuilder out = new StringBuilder(header.toString());
        out.append('.');
        out.append(payload);
        out.append(secretKey);
        return encryptThisString(out.toString());
        */
        //return encryptThisString(base64code(header) + "." + payload + secretKey);
        String secretKey64 = new String(Base64.getEncoder().encode(secretKey.getBytes()));
        return encryptThisString(header64 + "." + payload64 + "." + secretKey64);
    }

    /**
     * Функция получает хэш от входного текста
     * @param input Текст, который надо зашифровать.
     * @return Хэш-интерпритация текста.
     */
    private static String encryptThisString(String input) {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
