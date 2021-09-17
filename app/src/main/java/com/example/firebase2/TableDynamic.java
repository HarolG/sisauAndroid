package com.example.firebase2;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class TableDynamic {
    private TableLayout tableLayout;
    private Context context;
    private String[]header;
    private ArrayList<String[]> data;
    private TableRow tableRow;
    private TextView txtCell;
    private int indexC, indexR;
    private boolean multiColor=false;
    int firtColor, secondColor;

    public TableDynamic(TableLayout tableLayout, Context context) {
        this.tableLayout = tableLayout;
        this.context = context;
    }

    public void addHeader(String[]header) {
        this.header = header;
        createHeader();
    }

    public void addData(ArrayList<String[]>data) {
        this.data = data;
        createDataTable();
    }

    private void newRow() {
        tableRow = new TableRow(context);
        tableRow.setGravity(Gravity.CENTER);
    }

    private void newCell() {
        txtCell = new TextView(context);
        txtCell.setGravity(Gravity.CENTER);
        txtCell.setTextSize(25);
    }

    private void createHeader() {
        indexC = 0;
        newRow();
        tableRow.setBackgroundColor(Color.WHITE);
        tableRow.setPadding(30, 0, 30, 0);
        while (indexC < header.length) {
            newCell();
            txtCell.setTextColor(Color.BLACK);
            txtCell.setPadding(30, 0, 30, 0);
            txtCell.setText(header[indexC++]);
            tableRow.addView(txtCell, newTableRowParams());
        }

        tableLayout.addView(tableRow);
    }

    private void createDataTable() {
        String info;
        for (indexR=1; indexR<=header.length; indexR++) {
            this.tableLayout.removeAllViews();
            newRow();
            multiColor = !multiColor;
            for (indexC=0; indexC<header.length; indexC++) {
                newCell();
                String[] columns = data.get(indexR-1);
                int primerColor = Color.rgb(217, 217, 217);
                int segundoColor = Color.WHITE;

                info=(indexC<=columns.length)?columns[indexC]:"";
                txtCell.setText(info);
                txtCell.setTextColor(Color.BLACK);

                txtCell.setBackgroundColor((multiColor)?primerColor:segundoColor);
                tableRow.addView(txtCell, newTableRowParams());
            }
            tableLayout.addView(tableRow);
        }
    }

    private TableRow getRow(int index) {
        return (TableRow) tableLayout.getChildAt(index);
    }

    public void addItems(String[]item) {
        String info;
        data.add(item);
        indexC = 0;
        newRow();
        while (indexC<header.length) {
            newCell();
            info = (indexC<item.length)?item[indexC++]:"";
        }
        tableLayout.addView(tableRow, data.size()-1);
        reColoring();
    }

    public void backgroudHeader(int color) {
        indexC = 0;
        while (indexC < this.header.length) {
            txtCell = getCell(0, indexC++);
            txtCell.setBackgroundColor(color);
        }
    }

    public void backgroudData(int firtColor, int secondColor) {
        for (indexR=1; indexR<=header.length; indexR++) {
            multiColor = !multiColor;
            for (indexC=0; indexC<header.length; indexC++) {
                txtCell = getCell(indexR, indexC);
                txtCell.setBackgroundColor((multiColor)?firtColor:secondColor);
            }
        }

        this.firtColor = firtColor;
        this.secondColor = secondColor;
    }

    public void reColoring() {
        indexC = 0;
        multiColor=!multiColor;
        while (indexC < header.length) {
            txtCell = getCell(data.size()-1, indexC++);
            txtCell.setBackgroundColor((multiColor)?firtColor:secondColor);
        }
    }

    private TextView getCell(int rowIndex, int columnIndex) {
        txtCell = getCell(data.size()-1, indexC++);
        return (TextView) tableRow.getChildAt(columnIndex);
    }

    private TableRow.LayoutParams newTableRowParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.setMargins(1,1,1,1);
        params.weight=1;
        return  params;
    }
}
