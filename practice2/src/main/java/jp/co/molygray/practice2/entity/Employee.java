package jp.co.molygray.practice2.entity;

import java.time.LocalDate;

import jp.co.molygray.practice2.kbn.Gender;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Employee {

    private String sei;
    private String mei;
    private Gender gender;
    private LocalDate birthDate;
}
