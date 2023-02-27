/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.quarkiverse.poi.it;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

@Path("/poi")
@ApplicationScoped
public class POIResource {
    // add some rest methods here

    @GET
    @Path("/docx")
    public String docx() throws IOException {

        // Read MS Office files using Apache POI
        try (InputStream is = getClass().getResourceAsStream("hello_poi.docx")) {
            assert is != null;
            XWPFDocument doc = new XWPFDocument(is);
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            return extractor.getText();
        }
    }

    @GET
    @Path("/doc")
    public String doc() throws IOException {

        // Read MS Office files using Apache POI
        try (InputStream is = getClass().getResourceAsStream("hello_poi.doc")) {
            assert is != null;
            HWPFDocument doc = new HWPFDocument(is);
            WordExtractor extractor = new WordExtractor(doc);
            return extractor.getText();
        }
    }

    @GET
    @Path("/xlxs")
    public String xlxs() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE)) {
            SXSSFSheet sheet = workbook.createSheet();
            sheet.trackAllColumnsForAutoSizing();
            Row row = sheet.createRow(0);
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setWrapText(true);

            Cell cell = row.createCell(0);
            cell.setCellValue(new XSSFRichTextString("test"));
            cell.setCellStyle(style);
            sheet.autoSizeColumn(0);

            workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();

            workbook.write(baos);
        }
        // Read Excel created above
        try (Workbook workbook = WorkbookFactory.create(new ByteArrayInputStream(baos.toByteArray()))) {
            return workbook.getSheetAt(0).getRow(0).getCell(0).getStringCellValue();
        }
    }

}
