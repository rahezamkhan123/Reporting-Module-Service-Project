package com.alzohar.order.exporter;

import java.io.IOException;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alzohar.order.entity.Order;

public class OrderExcelExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<Order> listOrders;

	public OrderExcelExporter(List<Order> listOrder) {
		this.listOrders = listOrder;
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Orders");

		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		createCell(row, 0, "order_id", style);
		createCell(row, 1, "name", style);
		createCell(row, 2, "price", style);
		createCell(row, 3, "quantity", style);
		createCell(row, 4, "total", style);
		createCell(row, 5, "order_date", style);
	}

	private void createCell(Row row, int columnCount, Object value, CellStyle style) {
		sheet.autoSizeColumn(columnCount);
		Cell cell = row.createCell(columnCount);
		if (value instanceof Long) {
			cell.setCellValue((Long) value);
		} else if (value instanceof String) {
			cell.setCellValue((String) value);
		}
		cell.setCellStyle(style);
	}

	private void writeDataLines() {
		int rowCount = 1;

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(14);
		style.setFont(font);

		for (Order order : listOrders) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			createCell(row, columnCount++, order.getId(), style);
			createCell(row, columnCount++, order.getName(), style);
			createCell(row, columnCount++, order.getPrice(), style);
			createCell(row, columnCount++, order.getQuantity(), style);
			createCell(row, columnCount++, order.getTotal(), style);
			createCell(row, columnCount++, order.getOrderDate(), style);
		}
	}

	public void export(HttpServletResponse response) throws IOException {
		writeHeaderLine();
		writeDataLines();

		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		workbook.close();

		outputStream.close();
	}

}
