package com.subscript.billing.service;
import com.lowagie.text.*;import com.lowagie.text.pdf.*;import com.subscript.billing.entity.Invoice;
import org.springframework.beans.factory.annotation.Value;import org.springframework.stereotype.Service;
import java.io.*;import java.math.BigDecimal;
@Service public class InvoicePdfService {
 @Value("${app.billing.currency:INR}") private String currency;
 public byte[] generate(Invoice i){try{
  ByteArrayOutputStream out=new ByteArrayOutputStream();Document d=new Document(PageSize.A4,42,42,48,48);PdfWriter.getInstance(d,out);d.open();
  Font title=FontFactory.getFont(FontFactory.HELVETICA_BOLD,19);Font bold=FontFactory.getFont(FontFactory.HELVETICA_BOLD,10);
  d.add(new Paragraph("BRIGHTLOOP SOFTWARE",title));d.add(new Paragraph("SUBSCRIPTION INVOICE",bold));d.add(new Paragraph(" "));
  PdfPTable meta=new PdfPTable(2);meta.setWidthPercentage(100);
  cell(meta,"Invoice: "+i.getInvoiceNumber());cell(meta,"Date: "+i.getInvoiceDate());cell(meta,"Customer: "+i.getCustomerName());cell(meta,"Due: "+i.getDueDate());
  cell(meta,"Email: "+(i.getCustomerEmail()==null?"—":i.getCustomerEmail()));cell(meta,"Status: "+i.getStatus());d.add(meta);d.add(new Paragraph(" "));
  PdfPTable t=new PdfPTable(new float[]{5,1,2,2});t.setWidthPercentage(100);
  for(String h:new String[]{"Description","Qty","Unit price","Amount"}){PdfPCell c=new PdfPCell(new Phrase(h,bold));c.setPadding(7);t.addCell(c);}
  for(var l:i.getLines()){t.addCell(l.getDescription());t.addCell(String.valueOf(l.getQuantity()));t.addCell(money(l.getUnitPrice()));t.addCell(money(l.getLineTotal()));}
  d.add(t);d.add(new Paragraph(" "));PdfPTable totals=new PdfPTable(2);totals.setWidthPercentage(45);totals.setHorizontalAlignment(Element.ALIGN_RIGHT);
  total(totals,"Subtotal",i.getSubtotal());total(totals,"Discount",i.getDiscountAmount());total(totals,"Tax ("+i.getTaxRatePercent()+"%)",i.getTaxAmount());total(totals,"TOTAL",i.getTotal());total(totals,"Paid",i.getAmountPaid());total(totals,"Balance due",i.getBalanceDue());d.add(totals);
  if(i.getNotes()!=null&&!i.getNotes().isBlank()){d.add(new Paragraph(" "));d.add(new Paragraph("Notes: "+i.getNotes()));}
  d.add(new Paragraph("Thank you for your business."));d.close();return out.toByteArray();
 }catch(Exception e){throw new IllegalStateException("Could not generate invoice PDF.",e);}}
 private void cell(PdfPTable t,String s){PdfPCell c=new PdfPCell(new Phrase(s));c.setPadding(6);t.addCell(c);}
 private void total(PdfPTable t,String label,BigDecimal value){t.addCell(label);t.addCell(money(value));}
 private String money(BigDecimal v){return currency+" "+v.setScale(2,java.math.RoundingMode.HALF_UP);}
}
