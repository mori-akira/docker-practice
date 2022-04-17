package jp.co.molygray.practice2.kbn;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    Man("0", "男性"), Woman("1", "女性");

    private String code;
    private String display;

    public static Gender of(String code) {
	return Arrays.stream(Gender.values()).filter(e -> StringUtils.equals(code, e.getCode())).findFirst()
		.orElse(null);
    }

    public String toString() {
	return display;
    }
}
