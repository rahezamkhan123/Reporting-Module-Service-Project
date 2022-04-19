package com.alzohar.order.exporter;

import java.awt.Color;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.alzohar.order.entity.Order;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.*;

public class OrderPDFExporter {

	private List<Order> listOrders;

	public OrderPDFExporter(List<Order> listOrders) {
		super();
		this.listOrders = listOrders;
	}

	private void writeTableHeader(PdfPTable table) {
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.DARK_GRAY);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("Id", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Name", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Price", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Quantity", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("Total", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("orderDate", font));
		table.addCell(cell);
	}

	private void writeTableData(PdfPTable table) {
		for (Order order : listOrders) {
			table.addCell(String.valueOf(order.getId()));
			table.addCell(order.getName());
			table.addCell(String.valueOf(order.getPrice()));
			table.addCell(Integer.toString(order.getQuantity()));
			table.addCell(String.valueOf(order.getTotal()));
			table.addCell(String.valueOf(order.getOrderDate()));
		}
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLUE);

		Paragraph p = new Paragraph("List of Products", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(p);

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.5f, 3.5f, 3.0f, 3.0f, 1.5f });
		table.setSpacingBefore(10);

		writeTableHeader(table);
		writeTableData(table);

		document.add(table);

		document.close();
	}
}
