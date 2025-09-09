package controller;

import model.DocumentModel;
import view.FindReplaceDialog;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.awt.*;

public class FindReplaceController {
    private final DocumentModel model;
    private final JTextArea textArea;
    private FindReplaceDialog view;
    private int lastFoundIndex = -1;

    public FindReplaceController(DocumentModel model, JTextArea textArea) {
        this.model = model; this.textArea = textArea;
    }

    public void setView(FindReplaceDialog view) { this.view = view; }
    public FindReplaceController getController() { return this; }

    public void find() {
        String searchText = view.getFindText();
        if (searchText.isEmpty()) { JOptionPane.showMessageDialog(view, "Aranacak metin girin.", "Uyarı", JOptionPane.WARNING_MESSAGE); return; }
        String content = textArea.getText();
        String searchFor = view.isMatchCase() ? searchText : searchText.toLowerCase();
        String textToSearch = view.isMatchCase() ? content : content.toLowerCase();
        lastFoundIndex = textToSearch.indexOf(searchFor, lastFoundIndex + 1);
        if (lastFoundIndex == -1) { lastFoundIndex = textToSearch.indexOf(searchFor);
            if (lastFoundIndex == -1) { JOptionPane.showMessageDialog(view, "\"" + searchText + "\" bulunamadı.", "Bulunamadı", JOptionPane.INFORMATION_MESSAGE); textArea.getHighlighter().removeAllHighlights(); return; }
        }
        try { int endIndex = lastFoundIndex + searchFor.length();
            textArea.setCaretPosition(endIndex); textArea.moveCaretPosition(lastFoundIndex); textArea.getCaret().setSelectionVisible(true);
            textArea.getHighlighter().addHighlight(lastFoundIndex, endIndex, DefaultHighlighter.DefaultPainter);
        } catch (BadLocationException e) { e.printStackTrace(); }
    }

    public void replace() {
        if (lastFoundIndex != -1) {
            String replaceText = view.getReplaceText(); String searchText = view.getFindText();
            try { textArea.getDocument().remove(lastFoundIndex, searchText.length());
                textArea.getDocument().insertString(lastFoundIndex, replaceText, null);
                model.setContent(textArea.getText()); lastFoundIndex = -1; find();
            } catch (BadLocationException ex) { ex.printStackTrace(); }
        } else { find(); }
    }

    public void replaceAll() {
        String searchText = view.getFindText(); String replaceText = view.getReplaceText();
        if (searchText.isEmpty()) { JOptionPane.showMessageDialog(view, "Aranacak metin girin.", "Uyarı", JOptionPane.WARNING_MESSAGE); return; }
        String content = textArea.getText();
        String searchFor = view.isMatchCase() ? searchText : searchText.toLowerCase();
        String textToSearch = view.isMatchCase() ? content : content.toLowerCase();
        if (!textToSearch.contains(searchFor)) { JOptionPane.showMessageDialog(view, "\"" + searchText + "\" bulunamadı.", "Bulunamadı", JOptionPane.INFORMATION_MESSAGE); return; }
        StringBuilder sb = new StringBuilder(); int start = 0; int foundIndex; int count = 0;
        while ((foundIndex = textToSearch.indexOf(searchFor, start)) != -1) {
            sb.append(content, start, foundIndex); sb.append(replaceText); start = foundIndex + searchFor.length(); count++;
        }
        sb.append(content.substring(start)); textArea.setText(sb.toString()); model.setContent(textArea.getText());
        lastFoundIndex = -1; textArea.getHighlighter().removeAllHighlights();
        JOptionPane.showMessageDialog(view, count + " eşleşme değiştirildi.", "Tamamlandı", JOptionPane.INFORMATION_MESSAGE);
    }
}