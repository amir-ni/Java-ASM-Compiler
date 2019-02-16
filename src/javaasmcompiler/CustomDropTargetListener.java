package javaasmcompiler;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.*;

public class CustomDropTargetListener implements DropTargetListener {

    private final String[] dragDropExtensionFilter = {".asm"};

    private UI owner;

    CustomDropTargetListener(UI ui){
        owner = ui;
    }

    @Override
            public void dragEnter(DropTargetDragEvent dtde) {
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
            }

            @SuppressWarnings("ResultOfMethodCallIgnored")
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    Transferable tr = dtde.getTransferable();
                    DataFlavor[] flavors = tr.getTransferDataFlavors();
                    for (DataFlavor flavor : flavors) {
                        if (flavor.isFlavorJavaFileListType()) {
                            dtde.acceptDrop(dtde.getDropAction());
                            try {
                                String fileName = tr.getTransferData(flavor).toString().replace("[", "")
                                        .replace("]", "");

                                boolean extensionAllowed = false;
                                for (String aDragDropExtensionFilter : dragDropExtensionFilter) {
                                    if (fileName.endsWith(aDragDropExtensionFilter)) {
                                        extensionAllowed = true;
                                        break;
                                    }
                                }
                                if (!extensionAllowed)
                                    JOptionPane.showMessageDialog(null, "This file is not allowed for drag & drop",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                else {
                                    File tempedFile = new File(fileName);
                                    AsmCodeArea asmCodeArea = owner.addNewTextPanel(tempedFile.getName(), tempedFile.getPath());
                                    asmCodeArea.undoManager = new UndoManager();
                                    asmCodeArea.textArea.getDocument().addUndoableEditListener(t -> asmCodeArea.undoManager.addEdit(t.getEdit()));
                                }
                            } catch (HeadlessException | UnsupportedFlavorException | IOException ex) {
                                ex.printStackTrace();
                            }
                            dtde.dropComplete(true);
                            return;
                        }
                    }
                } catch (Throwable t) {
                    t.printStackTrace();
                }
                dtde.rejectDrop();
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
            }
}
