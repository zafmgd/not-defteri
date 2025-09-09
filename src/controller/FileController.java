package controller;

import model.DocumentModel;
import model.RecentFilesManager;
import view.MainFrame;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.nio.file.Path;

public class FileController {
    private final MainFrame view;
    private final DocumentModel model;
    private final RecentFilesManager recentFilesManager;
    private final JFileChooser fileChooser;

    public FileController(MainFrame view, DocumentModel model, RecentFilesManager recentFilesManager) {
        this.view = view;
        this.model = model;
        this.recentFilesManager = recentFilesManager;
        this.fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Metin Dosyaları (*.txt)", "txt"));
    }

    public void newFile() {
        if (model.isDirty()) {
            int option = JOptionPane.showConfirmDialog(view, "Değişiklikleri kaydetmek istiyor musunuz?", "Kaydedilsin mi?", JOptionPane.YES_NO_CANCEL_OPTION);
            if (option == JOptionPane.YES_OPTION) { saveFile(); } else if (option == JOptionPane.CANCEL_OPTION) { return; }
        }
        model.clear(); view.getTextArea().setText(""); view.updateTitle();
    }

    public void openFile() { openFile(null); }

    public void openFile(Path path) {
        if (model.isDirty()) {
            int option = JOptionPane.showConfirmDialog(view, "Değişiklikleri kaydetmek istiyor musunuz?", "Kaydedilsin mi?", JOptionPane.YES_NO_CANCEL_OPTION);
            if (option == JOptionPane.YES_OPTION) { if (!saveFile()) return; } else if (option == JOptionPane.CANCEL_OPTION) { return; }
        }
        if (path == null) { if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) path = fileChooser.getSelectedFile().toPath(); else return; }
        try {
            model.loadFromFile(path); view.getTextArea().setText(model.getContent());
            recentFilesManager.addRecentFile(path); view.updateRecentFilesMenu(); view.updateTitle();
        } catch (IOException e) { JOptionPane.showMessageDialog(view, "Dosya açılırken bir hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE); }
    }

    public boolean saveFile() {
        if (model.getCurrentFilePath() == null) { return saveFileAs(); }
        try { model.saveToFile(model.getCurrentFilePath()); view.updateTitle(); return true; }
        catch (IOException e) { JOptionPane.showMessageDialog(view, "Dosya kaydedilirken bir hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE); return false; }
    }

    public boolean saveFileAs() {
        if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            Path path = fileChooser.getSelectedFile().toPath();
            if (!path.toString().toLowerCase().endsWith(".txt")) path = path.resolveSibling(path.getFileName() + ".txt");
            try { model.saveToFile(path); recentFilesManager.addRecentFile(path); view.updateRecentFilesMenu(); view.updateTitle(); return true; }
            catch (IOException e) { JOptionPane.showMessageDialog(view, "Dosya kaydedilirken bir hata oluştu: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE); return false; }
        }
        return false;
    }

    public void exit() {
        if (model.isDirty()) {
            int option = JOptionPane.showConfirmDialog(view, "Değişiklikleri kaydetmek istiyor musunuz?", "Kaydedilsin mi?", JOptionPane.YES_NO_CANCEL_OPTION);
            if (option == JOptionPane.YES_OPTION) { if (saveFile()) { System.exit(0); } } else if (option == JOptionPane.NO_OPTION) { System.exit(0); }
        } else { System.exit(0); }
    }
}