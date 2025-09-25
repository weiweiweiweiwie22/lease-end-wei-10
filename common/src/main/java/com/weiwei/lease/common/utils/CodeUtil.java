package com.weiwei.lease.common.utils;

import java.util.concurrent.ThreadLocalRandom;

public class CodeUtil {

    /**
     * 生成指定长度的随机数字验证码
     *
     * @param length 验证码长度
     * @return 随机数字字符串
     */
    public static String getCode(Integer length) {
        // 1. 参数校验，确保长度大于0
        if (length == null || length <= 0) {
            // 可以返回空字符串或抛出异常，根据业务需求决定
            return "";
        }

        // 2. 使用 StringBuilder 来高效拼接字符串
        StringBuilder sb = new StringBuilder(length);

        // 3. 使用 ThreadLocalRandom 获取当前线程的随机数生成器
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // 4. 循环指定次数，生成每一位随机数
        for (int i = 0; i < length; i++) {
            // nextInt(10) 会生成一个 [0, 10) 的整数，即 0 到 9
            sb.append(random.nextInt(10));
        }

        // 5. 返回最终生成的字符串
        return sb.toString();
    }

    public static void main(String[] args) {
        // 测试生成一个6位数的验证码
        String code = getCode(6);
        System.out.println("生成的6位验证码是: " + code);

        // 测试生成一个4位数的验证码
        String code2 = getCode(4);
        System.out.println("生成的4位验证码是: " + code2);
    }
}
