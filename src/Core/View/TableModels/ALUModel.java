package Core.View.TableModels;

import javax.swing.table.AbstractTableModel;

public class ALUModel extends AbstractTableModel {
    String[] columnNames = {"ALU", "Input1", "Input2", "Output"};
    Object[][] data = {{"IF ALU", "-", "-", "-"}, {"Main ALU", "-","-", "-"},
            {"Branch ALU", "-", "-","-"}};

    @Override
    public int getRowCount() {
        return 3;
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
