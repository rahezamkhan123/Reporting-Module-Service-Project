package com.alzohar.products.exporter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import com.alzohar.products.entity.Product;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ProductPDFExporter {

	private List<Product> listProducts;

	public ProductPDFExporter(List<Product> listProducts) {
		super();
		this.listProducts = listProducts;
	}

	private void writeTableHeader(PdfPTable table) {
		Stream.of("Id", "Name", "Price", "Brand", "Description", "Enabled", "CreatedAt").forEach(headerTitle -> {
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
		for (Product product : listProducts) {
			table.addCell(String.valueOf(product.getId()));
			table.addCell(product.getName());
			table.addCell(String.valueOf(product.getPrice()));
			table.addCell(product.getBrand());
			table.addCell(product.getDesc());
			table.addCell(product.getEnabled());
			table.addCell(String.valueOf(product.getCreatedAt()));
		}
	}

	public void export(HttpServletResponse response) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE);
		font.setSize(12);
		font.setColor(BaseColor.DARK_GRAY);

		Paragraph p = new Paragraph("LIST OF PRODUCTS", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);

		document.add(p);

		PdfPTable table = new PdfPTable(7);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 1.5f, 3.5f, 3.0f, 3.0f, 3.5f, 2.5f, 3.5f });
		table.setSpacingBefore(8);

		writeTableHeader(table);
		writeTableData(table);

		document.add(table);

		document.close();
	}
}
