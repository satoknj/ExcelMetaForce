package jp.co.tv.excelmetaforce;

import jp.co.tv.excelmetaforce.excel.ObjectData;
import jp.co.tv.excelmetaforce.runner.Creater;

public final class CreateCustomObject {
    private CreateCustomObject() {}

    /**
     * Read Custom Object Definition from excel workbook, and save to salesforce. 
     * + object definition 
     * + field definition
     * + picklist definition
     * 
     * @param args arguments
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void main(String... args) {
        // set create target metadata
        Class[] target = new Class[]{
            ObjectData.class
        };
        String excelFileName = "オブジェクト定義2.xlsx";
        
        // execute target operation
        for (Class cls : target) {
            new Creater(cls, excelFileName).create();
        }
    }
}
