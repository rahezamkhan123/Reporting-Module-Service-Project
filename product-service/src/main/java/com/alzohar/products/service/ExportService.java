package com.alzohar.products.service;

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

import com.alzohar.products.entity.Product;
import com.alzohar.products.exporter.ProductExcelExporter;
import com.alzohar.products.exporter.ProductPDFExporter;
import com.alzohar.products.repository.ProductRepository;
import com.itextpdf.text.DocumentException;

@RestController
public class ExportService {

	@Autowired
	ProductRepository proRepository;

	@Autowired
	ProductService proService;

	public void exportToExcel(HttpServletResponse response) {

		try {
			response.setContentType("application/vnd.ms-excel");

			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=products.xlsx";
			response.setHeader(headerKey, headerValue);

			List<Product> listProduct = proService.listAll();
			ProductExcelExporter excelExporter = new ProductExcelExporter(listProduct);

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
			String headerValue = "attachment; filename=products_" + currentDateTime + ".csv";
			response.setHeader(headerKey, headerValue);

			List<Product> listProduct = proService.listAll();

			ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
			String[] csvHeader = { "Product_Id", "Product_Name", "Product_Price", "Product_Brand",
					"Product_Description", "Product_Enabled", "Product_Created_At" };
			String[] nameMapping = { "id", "name", "price", "brand", "desc", "enabled", "createdAt" };

			csvWriter.writeHeader(csvHeader);

			for (Product product : listProduct) {
				csvWriter.write(product, nameMapping);
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
			response.setHeader("Content-Disposition", "attachment ;filename=products.pdf");

			List<Product> listProduct = proService.listAll();

			ProductPDFExporter exporter = new ProductPDFExporter(listProduct);
			exporter.export(response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

}
