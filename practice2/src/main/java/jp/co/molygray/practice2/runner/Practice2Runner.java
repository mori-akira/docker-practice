package jp.co.molygray.practice2.runner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import jp.co.molygray.practice2.entity.Employee;
import jp.co.molygray.practice2.service.EmployeeDataService;
import jp.co.molygray.practice2.service.OutputService;

@SpringBootApplication
@ComponentScan("jp.co.molygray")
@Component
public class Practice2Runner implements ApplicationRunner {

    @Autowired
    private EmployeeDataService employeeDataService;
    @Autowired
    private OutputService outputService;

    @Override
    public void run(ApplicationArguments args) {
	try {
	    List<String> params = args.getNonOptionArgs();
	    Map<String, String> paramMap = new HashMap<>();
	    paramMap = params.stream().filter(e -> e.contains("="))
		    .collect(Collectors.toMap(e -> e.split("=")[0], e -> e.split("=")[1]));

	    List<Employee> employeeList = employeeDataService
		    .generateEmpolyeeData(Integer.valueOf(paramMap.get("generateNum")), paramMap.get("src"));
	    outputService.outputEmployeeList(employeeList);
	} catch (Exception ex) {
	    try {
		System.err.println(ex);
		outputService.outputMessage("処理に失敗");
		outputService.outputMessage(ex.getLocalizedMessage());
	    } catch (IOException ex2) {
		throw new RuntimeException(ex2);
	    }
	}
    }
}
