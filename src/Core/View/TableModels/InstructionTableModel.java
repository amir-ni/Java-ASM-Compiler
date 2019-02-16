package Core.View.TableModels;

import javax.swing.table.AbstractTableModel;

public class InstructionTableModel extends AbstractTableModel {
    String[] columnNames = {"Stage", "Instruction", "Binary Form"};
    Object[][] data = {{"IF","-","-"},
            {"ID","-","-"},
            {"EX","-","-"},
            {"MEM","-","-"},
            {"WB","-","-"}};

    @Override
    public int getRowCount() {
        return 5;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = value;
        fireTableCellUpdated(rowIndex, columnIndex);
    }


}
