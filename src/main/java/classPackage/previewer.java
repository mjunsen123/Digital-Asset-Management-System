package classPackage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;

//Word document
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

//Presentation
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;

//Spreadsheet
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.converter.ExcelToHtmlConverter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

//converter
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.openxml4j.util.ZipSecureFile;

public class previewer {

    public static void previewImagePDF(HttpServletRequest request, HttpServletResponse response, String path, String type) {
        try (OutputStream out = response.getOutputStream()) {
            response.setContentType(type);
            response.addHeader("Content-Disposition", "inline; filename=" + FilenameUtils.getName(path));
            BufferedOutputStream bout;
            try (FileInputStream fin = new FileInputStream(path); BufferedInputStream bin = new BufferedInputStream(fin)) {
                bout = new BufferedOutputStream(out);
                int ch;
                while ((ch = bin.read()) != -1) {
                    bout.write(ch);
                }
                fin.close();
            }
            bout.close();
            out.close();
            System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Previewing " + FilenameUtils.getName(path));
        } catch (IOException | NumberFormatException e) {
            System.err.println(e.getMessage());
        }

    }

    public static void previewPresentation(HttpServletRequest request, HttpServletResponse response, String path) {
        try {
            long startTime = System.nanoTime();
            ZipSecureFile.setMinInflateRatio(0);
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "inline; filename=" + FilenameUtils.getName(path));
            Document pdfDocument;
            PdfWriter pdfWriter;
            PdfPTable table;
            try (FileInputStream inputStream = new FileInputStream(path)) {
                double zoom = 2;
                AffineTransform at = new AffineTransform();
                at.setToScale(zoom, zoom);
                pdfDocument = new Document();
                OutputStream out = response.getOutputStream();
                pdfWriter = PdfWriter.getInstance(pdfDocument, out);
                table = new PdfPTable(1);
                pdfWriter.open();
                pdfDocument.open();
                Dimension pgsize;
                Image slideImage;
                BufferedImage img;
                if (digitalFiles.getExtention(new File(path)).equals("ppt")) {
                    HSLFSlideShow ppt = new HSLFSlideShow(inputStream);
                    pgsize = ppt.getPageSize();
                    List<HSLFSlide> slide = ppt.getSlides();
                    pdfDocument.setPageSize(new Rectangle((float) pgsize.getWidth(), (float) pgsize.getHeight()));
                    pdfWriter.open();
                    pdfDocument.open();
                    for (int i = 0; i < slide.size(); i++) {
                        img = new BufferedImage((int) Math.ceil(pgsize.width * zoom), (int) Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
                        Graphics2D graphics = img.createGraphics();
                        graphics.setTransform(at);
                        graphics.setPaint(Color.white);
                        graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                        slide.get(i).draw(graphics);
                        graphics.getPaint();
                        slideImage = Image.getInstance(img, null);
                        table.addCell(new PdfPCell(slideImage, true));
                    }
                }
                else if (digitalFiles.getExtention(new File(path)).equals("pptx")) {
                    XMLSlideShow ppt = new XMLSlideShow(inputStream);
                    pgsize = ppt.getPageSize();
                    List<XSLFSlide> slide = ppt.getSlides();
                    pdfDocument.setPageSize(new Rectangle((float) pgsize.getWidth(), (float) pgsize.getHeight()));
                    pdfWriter.open();
                    pdfDocument.open();
                    for (int i = 0; i < slide.size(); i++) {
                        img = new BufferedImage((int) Math.ceil(pgsize.width * zoom), (int) Math.ceil(pgsize.height * zoom), BufferedImage.TYPE_INT_RGB);
                        Graphics2D graphics = img.createGraphics();
                        graphics.setTransform(at);
                        graphics.setPaint(Color.white);
                        graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
                        slide.get(i).draw(graphics);
                        graphics.getPaint();
                        slideImage = Image.getInstance(img, null);
                        table.addCell(new PdfPCell(slideImage, true));
                    }
                    
                }
            }
            pdfDocument.add(table);
            pdfDocument.close();
            pdfWriter.close();
            long endTime = System.nanoTime();
            long timeElapsed = (endTime - startTime) / 1000000;
            System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Previewing " + FilenameUtils.getName(path) + "( " + timeElapsed + " millisecond(s) )");
        } catch (DocumentException | IOException e) {
            System.err.println(e.getMessage());
        }

    }

    public static void previewText(HttpServletRequest request, HttpServletResponse response, String path) {
        try {
            response.setContentType("text/html");
            try (InputStream is = new FileInputStream(path)) {
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);
                PrintWriter pw = response.getWriter();
                String text;
                while ((text = reader.readLine()) != null) {
                    pw.println(text + "<br>");
                }
            }
            System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Previewing " + FilenameUtils.getName(path));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    public static void previewDocx(HttpServletRequest request, HttpServletResponse response, String path) {
        try {
            long startTime = System.nanoTime();
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "inline; filename=" + FilenameUtils.getName(path));
            OutputStream out;
            XWPFDocument doc;
            long timeElapsed;
            try (InputStream fis = new FileInputStream(new File(path))) {
                out = response.getOutputStream();
                doc = new XWPFDocument(fis);
                PdfOptions options = PdfOptions.create();
                PdfConverter.getInstance().convert(doc, out, options);
                long endTime = System.nanoTime();
                timeElapsed = (endTime - startTime) / 1000000;
            }
            doc.close();
            out.close();
            System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Previewing " + FilenameUtils.getName(path) + "( " + timeElapsed + " millisecond(s) )");
        } catch (NotOfficeXmlFileException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void previewDoc(HttpServletRequest request, HttpServletResponse response, String path) {
        try {
            long startTime = System.nanoTime();
            response.setContentType("application/pdf");
            response.addHeader("Content-Disposition", "inline; filename=" + FilenameUtils.getName(path));
            //Extracting doc file
            Document document = new Document();
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(path));
            HWPFDocument doc = new HWPFDocument(fs);
            WordExtractor we = new WordExtractor(doc);
            we.getText();
            OutputStream out = response.getOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, out);
            Range range = doc.getRange();
            document.open();
            writer.setPageEmpty(true);
            document.newPage();
            writer.setPageEmpty(true);
            String[] paragraphs = we.getParagraphText();
            for (int i = 0; i < paragraphs.length; i++) {
                range.getParagraph(i);
                paragraphs[i] = paragraphs[i].replaceAll("\\cM?\r?\n", "");
                document.add(new Paragraph(paragraphs[i]));
            }
            long endTime = System.nanoTime();
            long timeElapsed = (endTime - startTime) / 1000000;
            document.close();
            doc.close();
            fs.close();
            System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Previewing " + FilenameUtils.getName(path) + "( " + timeElapsed + " millisecond(s) )");
        } catch (IOException | DocumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void previewSpreadsheet(HttpServletRequest request, HttpServletResponse response, String path) {
        try {
            response.setContentType("text/html");
            long startTime = System.nanoTime();
            switch (digitalFiles.getExtention(new File(path))) {
                case "xls":
                    {
                        OutputStream out;
                    try (FileInputStream fin = new FileInputStream(new File(path))) {
                        org.w3c.dom.Document doc = ExcelToHtmlConverter.process(fin);
                        out = response.getOutputStream();
                        DOMSource domSource = new DOMSource(doc);
                        StreamResult streamResult = new StreamResult(out);
                        TransformerFactory transfFactory = TransformerFactory.newInstance();
                        Transformer serializer = transfFactory.newTransformer();
                        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
                        serializer.setOutputProperty(OutputKeys.METHOD, "html");
                        serializer.transform(domSource, streamResult);
                    }
                        out.close();
                        break;
                    }

                case "xlsx":
                    {
                        PrintWriter pw;
                    try (FileInputStream fin = new FileInputStream(new File(path))) {
                        pw = response.getWriter();
                        if (digitalFiles.getExtention(new File(path)).equals("xlsx")) {
                            Workbook workbook = new XSSFWorkbook(fin);
                            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                                Sheet sheet = workbook.getSheetAt(i);
                                Iterator<Row> rowIterator = sheet.iterator();
                                pw.println("<h3>" + sheet.getSheetName() + "</h3>");
                                pw.println("<table style=\"border-collapse: collapse; border: 1px solid black;\">");
                                pw.println("<tbody>");
                                while (rowIterator.hasNext()) {
                                    pw.println("<tr style=\"border: 1px solid black;\">");
                                    Row row = rowIterator.next();
                                    Iterator<Cell> cellIterator = row.cellIterator();
                                    while (cellIterator.hasNext()) {
                                        Cell cell = cellIterator.next();
                                        switch (cell.getCellType()) {
                                            case NUMERIC:
                                            case FORMULA:
                                                pw.println("<td style=\"border: 1px solid black;\">" + cell.getNumericCellValue() + "</td>");
                                                break;
                                            case STRING:
                                                pw.println("<td style=\"border: 1px solid black;\">" + cell.getRichStringCellValue() + "</td>");
                                                break;
                                            case BOOLEAN:
                                                pw.println("<td style=\"border: 1px solid black;\">" + cell.getBooleanCellValue() + "</td>");
                                                break;
                                        }
                                    }
                                    pw.println("</tr>");
                                }
                                pw.println("</tbody>");
                                pw.println("</table>");
                                pw.println("<br>");
                            }
                        }
                    }
                        pw.close();
                        break;
                    }

                case "csv":
                    {
                        PrintWriter pw;
                    try (FileReader filereader = new FileReader(new File(path)); CSVReader csvReader = new CSVReader(filereader)) {
                        String[] nextRecord;
                        pw = response.getWriter();
                        pw.println("<table style=\"border-collapse: collapse; border: 1px solid black; width:auto;\">");
                        pw.println("<tbody width:auto;>");
                        while ((nextRecord = csvReader.readNext()) != null) {
                            pw.println("<tr style=\"border: 1px solid black; width:auto;\">");
                            for (String cell : nextRecord) {
                                pw.println("<td style=\"border: 1px solid black; width:auto;\">" + cell + "</td>");
                            }
                            pw.println("</tr>");
                        }
                        pw.println("</tbody>");
                        pw.println("</table>");
                    }
                        pw.close();
                        break;
                    }

                default:
                    break;
            }
            long endTime = System.nanoTime();
            long timeElapsed = (endTime - startTime) / 1000000;
            System.err.println(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss.SSS").format(LocalDateTime.now()) + " ACTION Previewing " + FilenameUtils.getName(path) + "( " + timeElapsed + " millisecond(s) )");
        } catch (IOException | IllegalArgumentException | TransformerException | ParserConfigurationException | CsvValidationException e) {
            System.err.println(e.getMessage());
        }

    }

}
