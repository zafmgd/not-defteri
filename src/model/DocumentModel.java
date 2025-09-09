package model;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DocumentModel {
    private String content = "";
    private Path currentFilePath = null;
    private boolean isDirty = false;
    private final UndoManager undoManager = new UndoManager();

    public DocumentModel() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        setDirty(true);
    }

    public Path getCurrentFilePath() {
        return currentFilePath;
    }

    public void setCurrentFilePath(Path currentFilePath) {
        this.currentFilePath = currentFilePath;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void setDirty(boolean dirty) {
        if (this.isDirty != dirty) {
            this.isDirty = dirty;
        }
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }

    public void loadFromFile(Path path) throws IOException {
        this.content = Files.readString(path);
        this.currentFilePath = path;
        this.isDirty = false;
    }

    public void saveToFile(Path path) throws IOException {
        Files.writeString(path, this.content);
        this.currentFilePath = path;
        this.isDirty = false;
    }

    public void clear() {
        this.content = "";
        this.currentFilePath = null;
        this.isDirty = false;
    }
}