package com.artezio.birt.emitters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;

import uk.co.spudsoft.birt.emitters.excel.EmitterServices;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;
import uk.co.spudsoft.birt.emitters.excel.HandlerState;
import uk.co.spudsoft.birt.emitters.excel.StyleManagerXUtils;

public class XlsxEmitter extends ExcelEmitter {

    public static final String HAS_COMMENTS = "ExcelEmitter.hasComments";
    public static final String COMMENT_PROPERTY_NAME = "ExcelEmitter.commentPropertyName";
    public static final String DEFAULT_COMMENT_PROPERTY_NAME = "excel_comment";
    private static final String AUTO_ROW_WIDTH_PROP = "ExcelEmitter.ForceAutoRowWidth";

    private HandlerState handlerState;
    private IRenderOption renderOptions;
    private String comment = "";
    private boolean hasComments;
    private String commentPropertyName;
    private boolean hasAutoRowWidth;

    public XlsxEmitter() {
        super(StyleManagerXUtils.getFactory());
    }

    public String getOutputFormat() {
        return "xlsx";
    }

    protected Workbook createWorkbook() {
        return new XSSFWorkbook();
    }

    protected Workbook openWorkbook(File templateFile) throws IOException {
        InputStream stream = new FileInputStream(templateFile);
        try {
            return new XSSFWorkbook(stream);
        } finally {
            stream.close();
        }
    }

    @Override
    public void endCell(ICellContent cell) throws BirtException {
        super.endCell(cell);

        if (hasComments && !comment.isEmpty()) {
            handlerState = getHandlerState();
            Cell currentCell = handlerState.currentSheet.getRow(handlerState.rowNum).getCell(cell.getColumn());
            setComment(currentCell, comment);
            comment = "";
        }
    }

    @Override
    public void initialize(IEmitterServices service) throws BirtException {
        super.initialize(service);
        renderOptions = service.getRenderOption();
        hasComments = EmitterServices.booleanOption(renderOptions, (IContent) null, HAS_COMMENTS, true);
        commentPropertyName = EmitterServices.stringOption(renderOptions, (IContent) null,
                COMMENT_PROPERTY_NAME, DEFAULT_COMMENT_PROPERTY_NAME);
        hasAutoRowWidth = EmitterServices.booleanOption(renderOptions, (IContent) null, AUTO_ROW_WIDTH_PROP, true);
    }

    @Override
    public void startData(IDataContent data) throws BirtException {
        super.startData(data);
        if (hasComments) {
            Map<String, Object> properties = data.getUserProperties();
            if (properties != null && properties.containsKey(commentPropertyName)) {
                comment = properties.get(commentPropertyName).toString();
            }
        }
    }

    @Override
    public void endTable(ITableContent table) throws BirtException {
        super.endTable(table);
        if (hasAutoRowWidth) {
            Sheet sheet = getHandlerState().currentSheet;
            for (Row row : sheet) {
                row.setHeight((short) -1);
            }
        }
    }

    private HandlerState getHandlerState() {
        if (handlerState != null)
            return handlerState;
        try {
            Field handlerStateField = ExcelEmitter.class.getDeclaredField("handlerState");
            handlerStateField.setAccessible(true);
            return (HandlerState) handlerStateField.get(this);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setComment(Cell cell, String commentText) {
        CreationHelper factory = getHandlerState().getWb().getCreationHelper();
        Sheet sheet = getHandlerState().currentSheet;
        Drawing drawing = sheet.createDrawingPatriarch();

        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(cell.getColumnIndex() + 2);
        anchor.setCol2(cell.getColumnIndex() + 5);
        anchor.setRow1(cell.getRowIndex() - 1);
        anchor.setRow2(cell.getRowIndex() + 3);

        Comment comment = drawing.createCellComment(anchor);
        RichTextString str = factory.createRichTextString(commentText);
        comment.setString(str);

        cell.setCellComment(comment);
    }

}
