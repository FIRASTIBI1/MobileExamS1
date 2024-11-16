package com.example.myapplicationjdid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ViewExcelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewexcelactivity);

        // Retrieve the Excel data (base64 decoded)
        byte[] excelData = getIntent().getByteArrayExtra("excelData");

        // Try to open and display the Excel content
        if (excelData != null) {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(excelData);
                Workbook workbook = WorkbookFactory.create(byteArrayInputStream);
                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

                if (sheet != null) {
                    Log.d("ViewExcelActivity", "Sheet loaded: " + sheet.getSheetName());
                    Row row = sheet.getRow(0);
                    if (row != null) {
                        Cell cell = row.getCell(0); // First cell
                        if (cell != null) {
                            String cellValue = cell.toString();
                            Log.d("ViewExcelActivity", "Cell Value: " + cellValue);

                            // Find the TextView and set the content
                            TextView textView = findViewById(R.id.textViewExcelContent);
                            textView.setText("Excel First Cell Value: " + cellValue);
                        } else {
                            Log.e("ViewExcelActivity", "Cell is null!");
                        }
                    } else {
                        Log.e("ViewExcelActivity", "Row is null!");
                    }
                } else {
                    Log.e("ViewExcelActivity", "Sheet is null!");
                }
            } catch (IOException e) {
                Log.e("ViewExcelActivity", "Error reading Excel file", e);

            }

        }
    }
}
