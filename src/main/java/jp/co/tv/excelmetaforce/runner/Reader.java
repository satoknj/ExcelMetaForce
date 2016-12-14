package jp.co.tv.excelmetaforce.runner;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.sforce.soap.metadata.Metadata;

import jp.co.tv.excelmetaforce.excel.SheetData;

public class Reader {
    private Workbook book;
    private SheetData data;
    private final String excelFileName;
    private final File inputFile;
    private File stashFile;
    
    /**
     * init by SheetData instance.
     * 
     * @param dataCls SheetData instance
     */
    public Reader(Class<? extends SheetData> dataCls, String excelFileName) {
        this.excelFileName = excelFileName;
        inputFile = new File(excelFileName);

        try {
            stashFile = new File(excelFileName + "_1");
            inputFile.renameTo(stashFile);
            book = WorkbookFactory.create(stashFile);
            this.data = dataCls.getDeclaredConstructor(Workbook.class).newInstance(book);
        } catch (Exception e) {
            closeWorkbook();
            stashFile.renameTo(inputFile);
            throw new RuntimeException(e);
        }
    }

    /**
     * Write object definition to excel file
     */
    public void read() {
        try {
            Metadata[] metadata = data.getTargetMetadata();
            data.write(metadata);
            
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(excelFileName));
            book.write(bos);
            bos.close();
            
            closeWorkbook();
            stashFile.deleteOnExit();
        } catch (Exception e) {
            closeWorkbook();
            stashFile.renameTo(inputFile);
            throw new RuntimeException(e);
        }
    }
    
    private void closeWorkbook() {
        try {
            book.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
