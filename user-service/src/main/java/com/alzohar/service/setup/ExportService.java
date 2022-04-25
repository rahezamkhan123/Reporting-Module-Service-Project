package com.alzohar.service.setup;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.alzohar.service.entity.User;
import com.alzohar.service.entity.UserService;
import com.alzohar.service.exporter.UserExcelExporter;
import com.alzohar.service.exporter.UserPdfExporter;
import com.alzohar.service.repository.UserRepository;
import com.itextpdf.text.DocumentException;

@RestController
public class ExportService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserService useService;

	@Autowired
	SetUserService service;

	public void exportToExcel(HttpServletResponse response) {
		try {
			response.setContentType("application/vnd.ms-excel");

			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=users.xlsx";
			response.setHeader(headerKey, headerValue);

			List<User> listUser = service.listAll();
			UserExcelExporter excelExporter = new UserExcelExporter(listUser);
			excelExporter.export(response);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void exportToCSV(HttpServletResponse response) {
		try {
			response.setContentType("text/csv");
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
			String currentDateTime = dateFormatter.format(new Date());

			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
			response.setHeader(headerKey, headerValue);

			List<User> listUser = service.listAll();

			ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
			String[] csvHeader = { "User_Id", "User_Username", "User_Password", "User_Enabled" };
			String[] nameMapping = { "id", "username", "password", "enabled" };

			csvWriter.writeHeader(csvHeader);

			for (User user : listUser) {
				csvWriter.write(user, nameMapping);
			}

			csvWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exportToPDF(HttpServletResponse response) {
		try {
			response.setContentType("application/pdf");
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String currentDateTime = dateFormatter.format(new Date());

			Date date = new Date();

			String headerKey = "Content-Disposition";
			String headerValue = "inline; filename=products" + currentDateTime + ".pdf";
			response.setHeader("Content-Disposition", "attachment ;filename=users.pdf");

			List<User> listUser = service.listAll();

			UserPdfExporter exporter = new UserPdfExporter(listUser);
			exporter.export(response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
