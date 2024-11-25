package com.example.myapplicationjdid.Agent;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationjdid.R;

import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ViewExcelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewexcelactivity);

        TableLayout tableLayout = findViewById(R.id.tableLayout);

        // Retrieve the Excel data (base64 decoded)
        byte[] excelData = getIntent().getByteArrayExtra("excelData");

        if (excelData != null) {
            try {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(excelData);
                Workbook workbook = WorkbookFactory.create(byteArrayInputStream);
                Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

                // Parse rows and cells
                for (Row row : sheet) {
                    TableRow tableRow = new TableRow(this);
                    tableRow.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    for (Cell cell : row) {
                        TextView textView = new TextView(this);
                        textView.setText(cell.toString());
                        textView.setPadding(8, 8, 8, 8);
                        textView.setBackgroundColor(Color.WHITE);
                        textView.setTextColor(Color.BLACK);
                        textView.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        tableRow.addView(textView);
                    }

                    // Add row to the TableLayout
                    tableLayout.addView(tableRow);
                }

                workbook.close();
            } catch (IOException e) {
                Log.e("ViewExcelActivity", "Error reading Excel file", e);
            }
        }
    }
}
