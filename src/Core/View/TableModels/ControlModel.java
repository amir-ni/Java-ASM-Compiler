package Core.View.TableModels;

import javax.swing.table.AbstractTableModel;

public class ControlModel extends AbstractTableModel {
    String[] columnNames = {"Name", "Value"};
    Object[][] data = {{"PCSrc", "-"}, {"IF.Flush","-"}, {"Stall Controls MUX", "-"},
            {"ALUSrc","-"}, {"ALUOp","-"},{"RegDst","-"},{"ForwardA","--"},{"ForwardB","--"},
            {"Branch","-"},{"MemRead","-"},{"MemWrite","-"},{"MemToReg","-"},{"RegWrite","-"}};

    @Override
    public int getRowCount() {
        return 13;
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
        if (value instanceof Integer &&(int)value == -1)
            data[rowIndex][columnIndex] = "-";
        else
            data[rowIndex][columnIndex] = value;
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
