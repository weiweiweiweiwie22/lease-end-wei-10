package com.weiwei.lease.web.admin.converter;

import com.weiwei.lease.model.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
public class StringToBaseEnumConverterFactory implements ConverterFactory<String, BaseEnum> {
    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        // 返回一个具体的转换器实例，这里使用 lambda 表达式简化代码
        return source -> {
            // 遍历目标枚举的所有实例
            for (T enumConstant : targetType.getEnumConstants()) {
                // 比较 code 值
                if (enumConstant.getCode().equals(Integer.valueOf(source))) {
                    return enumConstant;
                }
            }
            // 如果没有找到匹配的，抛出异常
            throw new IllegalArgumentException("无效的枚举值: " + source);
        };
    }
}
