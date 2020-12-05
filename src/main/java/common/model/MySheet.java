package main.java.common.model;

import java.util.ArrayList;
import java.util.List;

public class MySheet {
   private String name; // sheet name
   private List<List<String>> content; // data matrix

   public MySheet(String name, int rowCount) {
      this.name = name;
      this.content = new ArrayList<>(1024);

      for (int i = 0; i < rowCount; ++i) {
         this.content.add(new ArrayList<>());
      }
   }

   public List<List<String>> getContent() {
      return content;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void addCellAtRow(int rowIndex, String value) {
      this.content.get(rowIndex).add(value);
   }

   public int getRowCount() {
      return this.content.size();
   }

   public String getValueAt(int rowIndex, int colIndex) {
      return this.content.get(rowIndex).get(colIndex);
   }

   public void addRow(List<String> cols) {
      this.content.add(cols);
   }

   public List<String> getRowAt(int rowIndex) {
      return this.content.get(rowIndex);
   }

   public boolean isExistCell(int rowIndex, int colIndex) {
      return rowIndex >= 0 && rowIndex < this.content.size() && colIndex >= 0 && colIndex < this.content.get(rowIndex).size();
   }
}
