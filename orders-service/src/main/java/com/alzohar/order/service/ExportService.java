package com.alzohar.order.service;

import java.io.IOException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.alzohar.order.entity.Order;
import com.alzohar.order.exporter.OrderExcelExporter;
import com.alzohar.order.exporter.OrderPDFExporter;
import com.alzohar.order.repository.OrderRepository;
import com.itextpdf.text.DocumentException;

@RestController
public class ExportService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	OrderService orderService;

	public void exportToExcel(HttpServletResponse response) {
		try {
			response.setContentType("application/vnd.ms-excel");

			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=orders.xlsx";
			response.setHeader(headerKey, headerValue);

			List<Order> listOrder = orderService.listAll();
			OrderExcelExporter excelExporter = new OrderExcelExporter(listOrder);

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
			String headerValue = "attachment; filename=orders_" + currentDateTime + ".csv";
			response.setHeader(headerKey, headerValue);

			List<Order> listOrder = orderService.listAll();

			ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
			String[] csvHeader = { "Order_Id", "Order_Name", "Order_Price", "Order_Quantity", "Order_Total",
					"Order_Order_Date" };
			String[] nameMapping = { "id", "name", "price", "quantity", "total", "orderDate" };

			csvWriter.writeHeader(csvHeader);

			for (Order order : listOrder) {
				csvWriter.write(order, nameMapping);
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
			String headerValue = "inline; filename=orders" + currentDateTime + ".pdf";
			response.setHeader("Content-Disposition", "attachment ;filename=orders.pdf");

			List<Order> listOrder = orderService.listAll();

			OrderPDFExporter exporter = new OrderPDFExporter(listOrder);
			exporter.export(response);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

}
