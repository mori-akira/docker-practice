package jp.co.molygray.practice2.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import jp.co.molygray.practice2.entity.Employee;

@Service
public class OutputService {

    private static final Path OUTPUT_PATH = Paths.get("/docker_sample/practice2/out/practice2.log");

    public void outputMessage(String message) throws IOException {
	Files.write(OUTPUT_PATH, LocalDateTime.now().toString().getBytes(), StandardOpenOption.APPEND,
		StandardOpenOption.CREATE);
	Files.write(OUTPUT_PATH, "\n".getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
	Files.write(OUTPUT_PATH, message.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
	Files.write(OUTPUT_PATH, "\n".getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
    }

    public void outputEmployeeList(List<Employee> employeeList) throws IOException {
	Files.write(OUTPUT_PATH, LocalDateTime.now().toString().getBytes(), StandardOpenOption.APPEND,
		StandardOpenOption.CREATE);
	Files.write(OUTPUT_PATH, "\n".getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
	for (Employee e : employeeList) {
	    Files.write(OUTPUT_PATH, e.toString().getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
	    Files.write(OUTPUT_PATH, "\n".getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
	}
    }
}
