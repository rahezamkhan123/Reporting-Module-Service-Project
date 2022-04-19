package com.alzohar.products.exporter;

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

import com.alzohar.products.entity.Product;

public class ProductExcelExporter {

	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private List<Product> listProducts;

	public ProductExcelExporter(List<Product> listProduct) {
		this.listProducts = listProduct;
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine() {
		sheet = workbook.createSheet("Products");

		Row row = sheet.createRow(0);

		CellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(16);
		style.setFont(font);

		createCell(row, 0, "product_id", style);
		createCell(row, 1, "name", style);
		createCell(row, 2, "price", style);
		createCell(row, 3, "desc", style);
		createCell(row, 4, "brand", style);
		createCell(row, 5, "enabled", style);
		createCell(row, 6, "created_At", style);
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

		for (Product prod : listProducts) {
			Row row = sheet.createRow(rowCount++);
			int columnCount = 0;

			createCell(row, columnCount++, prod.getId(), style);
			createCell(row, columnCount++, prod.getName(), style);
			createCell(row, columnCount++, prod.getPrice(), style);
			createCell(row, columnCount++, prod.getDesc(), style);
			createCell(row, columnCount++, prod.getBrand(), style);
			createCell(row, columnCount++, prod.getEnabled(), style);
			createCell(row, columnCount++, prod.getCreatedAt(), style);
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
