package jp.co.molygray.practice2.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jp.co.molygray.practice2.entity.Employee;
import jp.co.molygray.practice2.kbn.Gender;

@Service
public class EmployeeDataService {

    private static final String FILE_NAME_SEI = "sei.dat";
    private static final String FILE_NAME_MEI_MAN = "mei_man.dat";
    private static final String FILE_NAME_MEI_WOMAN = "mei_woman.dat";
    private static final LocalDate START_DATE = LocalDate.of(1900, 1, 1);
    private static final LocalDate END_DATE = LocalDate.of(2010, 1, 1);

    public List<Employee> generateEmpolyeeData(Integer num, String dataSrc) throws Exception {
	List<Employee> ret = new ArrayList<>();
	for (int i = 0; i < num; i++) {
	    Employee e = new Employee();
	    Gender gender = Gender.of(String.valueOf(randomInt(2)));
	    e.setGender(gender);
	    List<String> seiList = getSeiData(dataSrc);
	    List<String> meiList = getMeiData(dataSrc, gender);
	    e.setSei(seiList.get(randomInt(seiList.size())));
	    e.setMei(meiList.get(randomInt(meiList.size())));
	    e.setBirthDate(LocalDate.ofEpochDay(randomLong(START_DATE.toEpochDay(), END_DATE.toEpochDay())));
	    ret.add(e);
	}
	return ret;
    }

    private List<String> getSeiData(String dataSrc) throws IOException {
	Path srcPath = Paths.get(dataSrc).resolve(FILE_NAME_SEI);
	return Files.lines(srcPath).collect(Collectors.toList());
    }

    private List<String> getMeiData(String dataSrc, Gender gender) throws IOException {
	if (gender == Gender.Man) {
	    return getManMeiData(dataSrc);
	} else if (gender == Gender.Woman) {
	    return getWomanMeiData(dataSrc);
	} else {
	    throw new IllegalArgumentException(String.format("不正な性別: %s", gender));
	}
    }

    private List<String> getManMeiData(String dataSrc) throws IOException {
	Path srcPath = Paths.get(dataSrc).resolve(FILE_NAME_MEI_MAN);
	return Files.lines(srcPath).collect(Collectors.toList());
    }

    private List<String> getWomanMeiData(String dataSrc) throws IOException {
	Path srcPath = Paths.get(dataSrc).resolve(FILE_NAME_MEI_WOMAN);
	return Files.lines(srcPath).collect(Collectors.toList());
    }

    private int randomInt(int end) {
	return randomInt(0, end);
    }

    private int randomInt(int start, int end) {
	return (int) (Math.random() * (end - start)) + start;
    }

    private long randomLong(long start, long end) {
	return (long) (Math.random() * (end - start)) + start;
    }
}
