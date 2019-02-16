package Core.View.TableModels;

import javax.swing.table.AbstractTableModel;

public class RegisterFileModel extends AbstractTableModel {
    String[] columnNames = {"Name", "Value", "Binary Form"};
    Object[][] data = {{"Read Register 1", "-","-"}, {"Read Register 2","-","-"},
            {"Read Data 1","-","-"}, {"Read Data 2","-","-"}};

    @Override
    public int getRowCount() {
        return 4;
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
