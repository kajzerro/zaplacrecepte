package com.hastlin.zaplacrecepte.utils;


import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_256;

public class BMUtils {

    private BMUtils() {
    }

    public static String calcHash(String... args) {
        String dataToEncode = Arrays.stream(args).filter(it -> !StringUtils.isEmpty(it)).collect(Collectors.joining("|"));
        return new DigestUtils(SHA_256).digestAsHex(dataToEncode);
    }

    public static String extractValueFromXmlTag(String data, String startTag, String endTag) {
        int indexOfStartTag = data.indexOf(startTag);
        int indexOfEndTag = data.indexOf(endTag);
        if (indexOfStartTag == -1 || indexOfEndTag == -1) {
            return null;
        }
        return data.substring(indexOfStartTag + startTag.length(), indexOfEndTag);
    }

    public static String formatPrice(int price) {
        return price + ".00";
    }

}
