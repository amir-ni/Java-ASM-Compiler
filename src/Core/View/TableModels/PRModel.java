package Core.View.TableModels;

import javax.swing.table.AbstractTableModel;

public class PRModel extends AbstractTableModel {
    String[] columnNames = {"Name", "PR", "Value(BinaryForm)"};
    Object[][] data = {{"PC+4", "IF/ID", "-"},
            {"Instruction", "IF/ID", "-"},
            {"PC+4", "ID/EX", "-"},
            {"Register Number 1", "ID/EX", "-"},
            {"Register Number 2", "ID/EX", "-"},
            {"Read Data 1", "ID/EX", "-"},
            {"Read Data 2", "ID/EX", "-"},
            {"Rt Register", "ID/EX", "-"},
            {"Rd Register", "ID/EX", "-"},
            {"32bit offset", "ID/EX", "-"},
            {"Branch Target", "EX/MEM", "-"},
            {"Zero", "EX/MEM", "-"},
            {"ALU Result", "EX/MEM", "-"},
            {"Read Data 2", "EX/MEM", "-"},
            {"Destination Register", "EX/MEM", "-"},
            {"Read Data", "MEM/WB", "-"},
            {"ALU Result", "MEM/WB", "-"},
            {"Destination Register", "MEM/WB", "-"}
    };

    @Override
    public int getRowCount() {
        return 18;
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
