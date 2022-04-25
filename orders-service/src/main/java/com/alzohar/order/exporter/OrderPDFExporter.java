package com.alzohar.order.exporter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import com.alzohar.order.entity.Order;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class OrderPDFExporter {

	private List<Order> listOrders;

	public OrderPDFExporter(List<Order> listOrders) {
		super();
		this.listOrders = listOrders;
	}

	private void writeTableHeader(PdfPTable table) {
		Stream.of("Id", "Name", "Price", "Quantity", "Total", "orderDate").forEach(headerTitle -> {
			PdfPCell header = new PdfPCell();
			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(headerTitle, headFont));
			table.addCell(header);
		});

	}

	private void writeTableData(PdfPTable table) {
		for (Order order : listOrders) {
			table.addCell(String.valueOf(order.getId()));
			table.addCell(order.getName());
			table.addCell(String.valueOf(order.getPrice()));
			table.addCell(String.valueOf(order.getQuantity()));
			table.addCell(String.valueOf(order.getTotal()));
			table.addCell(String.valueOf(order.getOrderDate()));
		}
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE);
		font.setSize(12);
		font.setColor(BaseColor.DARK_GRAY);
		Paragraph p = new Paragraph("LIST OF ORDERS", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(p);

		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.5f, 2.5f, 2.5f, 1.5f, 2.0f, 3.0f });
		table.setSpacingBefore(8);

		writeTableHeader(table);
		writeTableData(table);

		document.add(table);

		document.close();
	}
}
