
package com.viettel.vtskit.easypoi.poi.word.entity;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Expand document to fix the problem of image insertion failure
 *
 * @author caprocute
 * @version 1.0
 * @date 2013-11-20
 */
public class MyXWPFDocument extends XWPFDocument {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyXWPFDocument.class);

    private static String PICXML = "" + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
            + "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
            + "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
            + "         <pic:nvPicPr>"
            + "            <pic:cNvPr id=\"%s\" name=\"Generated\"/>"
            + "            <pic:cNvPicPr/>"
            + "         </pic:nvPicPr>"
            + "         <pic:blipFill>"
            + "            <a:blip r:embed=\"%s\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
            + "            <a:stretch>"
            + "               <a:fillRect/>"
            + "            </a:stretch>"
            + "         </pic:blipFill>"
            + "         <pic:spPr>"
            + "            <a:xfrm>"
            + "               <a:off x=\"0\" y=\"0\"/>"
            + "               <a:ext cx=\"%s\" cy=\"%s\"/>"
            + "            </a:xfrm>"
            + "            <a:prstGeom prst=\"rect\">"
            + "               <a:avLst/>"
            + "            </a:prstGeom>"
            + "         </pic:spPr>"
            + "      </pic:pic>"
            + "   </a:graphicData>"
            + "</a:graphic>";

    public MyXWPFDocument() {
        super();
    }

    public MyXWPFDocument(InputStream in) throws Exception {
        super(in);
    }

    public MyXWPFDocument(OPCPackage opcPackage) throws Exception {
        super(opcPackage);
    }

    public void createPicture(String blipId, int id, int width, int height) {
        final int EMU = 9525;
        width *= EMU;
        height *= EMU;
        CTInline inline = createParagraph().createRun().getCTR().addNewDrawing().addNewInline();
        String picXml = String.format(PICXML, id, blipId, width, height);
        XmlToken xmlToken = null;
        try {
            xmlToken = XmlToken.Factory.parse(picXml);
        } catch (XmlException xe) {
            LOGGER.error(xe.getMessage(), xe.fillInStackTrace());
        }
        inline.set(xmlToken);

        inline.setDistT(0);
        inline.setDistB(0);
        inline.setDistL(0);
        inline.setDistR(0);

        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx(width);
        extent.setCy(height);

        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId(id);
        docPr.setName("Picture " + id);
        docPr.setDescr("Generated");
    }

    public void createPicture(XWPFRun run, String blipId, int id, int width, int height) {
        final int EMU = 9525;
        width *= EMU;
        height *= EMU;
        CTInline inline = run.getCTR().addNewDrawing().addNewInline();
        String picXml = String.format(PICXML, id, blipId, width, height);
        XmlToken xmlToken = null;
        try {
            xmlToken = XmlToken.Factory.parse(picXml);
        } catch (XmlException xe) {
            LOGGER.error(xe.getMessage(), xe.fillInStackTrace());
        }
        inline.set(xmlToken);

        inline.setDistT(0);
        inline.setDistB(0);
        inline.setDistL(0);
        inline.setDistR(0);

        CTPositiveSize2D extent = inline.addNewExtent();
        extent.setCx(width);
        extent.setCy(height);

        CTNonVisualDrawingProps docPr = inline.addNewDocPr();
        docPr.setId(id);
        docPr.setName("Picture " + id);
        docPr.setDescr("Generated");
    }

}
