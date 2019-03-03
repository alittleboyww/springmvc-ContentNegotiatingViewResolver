package com.liu.springmvc.viewresolver;

import com.liu.springmvc.Model.Pizza;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Map;

public class PdfView extends AbstractPdfView{
    @Override
    protected void buildPdfDocument(Map<String, Object> map, Document document, PdfWriter pdfWriter, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Pizza pizza = (Pizza)map.get("pizza");

        PdfPTable table = new PdfPTable(3);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.getDefaultCell().setBackgroundColor(Color.lightGray);

        table.addCell("Name");
        table.addCell("Flavor");
        table.addCell("Toppings");

        table.addCell(pizza.getName());
        table.addCell(pizza.getFlavor());
        StringBuffer toppings = new StringBuffer();
        for (String topping: pizza.getToppings()){
            toppings.append(topping);
            toppings.append(" ");
        }
        table.addCell(toppings.toString());
        document.add(table);
    }
}
