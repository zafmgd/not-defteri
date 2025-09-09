package view;

import controller.*;
import model.DocumentModel;
import model.RecentFilesManager;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class MainFrame extends JFrame {
    private final DocumentModel model;
    private final RecentFilesManager recentFilesManager;
    private final JTextArea textArea;
    private final StatusBar statusBar;
    private final FindReplaceDialog findReplaceDialog;
    private final FileController fileController;
    private final FindReplaceController findReplaceController;
    private final FontController fontController;
    private ThemeController themeController; // final kaldırıldı, çünkü initUI içinde yeniden oluşturulacak
    private JMenu recentFilesMenu;

    public MainFrame() {
        super("Profesyonel Not Defteri");
        this.model = new DocumentModel();
        this.recentFilesManager = new RecentFilesManager();
        this.textArea = new JTextArea();
        this.statusBar = new StatusBar();

        FindReplaceController findReplaceControllerInstance = new FindReplaceController(model, textArea);
        this.findReplaceDialog = new FindReplaceDialog(this, findReplaceControllerInstance);
        this.findReplaceController = findReplaceControllerInstance;
        this.fileController = new FileController(this, model, recentFilesManager);
        this.fontController = new FontController(textArea);
        
        // themeController burada oluşturulmayacak, initUI içinde oluşturulacak
        
        initUI();
        setupListeners();
        updateTitle();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JMenuBar menuBar = createMenuBar();
        
        setLayout(new BorderLayout());
        this.setJMenuBar(menuBar);
        add(scrollPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        // Menü çubuğu oluşturulduktan sonra themeController'ı doğru şekilde oluştur
        this.themeController = new ThemeController(this, textArea, menuBar, statusBar);
        this.themeController.applyInitialTheme();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Dosya");
        fileMenu.setMnemonic(KeyEvent.VK_D);
        JMenuItem newItem = new JMenuItem("Yeni", KeyEvent.VK_N);
        JMenuItem openItem = new JMenuItem("Aç...", KeyEvent.VK_O);
        JMenuItem saveItem = new JMenuItem("Kaydet", KeyEvent.VK_S);
        JMenuItem saveAsItem = new JMenuItem("Farklı Kaydet...");
        JMenuItem exitItem = new JMenuItem("Çıkış");
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        recentFilesMenu = new JMenu("Son Dosyalar");
        updateRecentFilesMenu();
        fileMenu.add(recentFilesMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        JMenu editMenu = new JMenu("Düzen");
        editMenu.setMnemonic(KeyEvent.VK_E);
        JMenuItem undoItem = new JMenuItem("Geri Al", KeyEvent.VK_Z);
        JMenuItem redoItem = new JMenuItem("İleri Al", KeyEvent.VK_Y);
        JMenuItem cutItem = new JMenuItem("Kes", KeyEvent.VK_X);
        JMenuItem copyItem = new JMenuItem("Kopyala", KeyEvent.VK_C);
        JMenuItem pasteItem = new JMenuItem("Yapıştır", KeyEvent.VK_V);
        JMenuItem findReplaceItem = new JMenuItem("Bul/Değiştir...", KeyEvent.VK_F);
        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.addSeparator();
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.addSeparator();
        editMenu.add(findReplaceItem);
        JMenu formatMenu = new JMenu("Biçim");
        JMenuItem fontItem = new JMenuItem("Yazı Tipi...");
        formatMenu.add(fontItem);
        JMenu viewMenu = new JMenu("Görünüm");
        JCheckBoxMenuItem themeToggle = new JCheckBoxMenuItem("Karanlık Mod");
        viewMenu.add(themeToggle);
        newItem.addActionListener(e -> fileController.newFile());
        openItem.addActionListener(e -> fileController.openFile());
        saveItem.addActionListener(e -> fileController.saveFile());
        saveAsItem.addActionListener(e -> fileController.saveFileAs());
        exitItem.addActionListener(e -> fileController.exit());
        undoItem.addActionListener(e -> model.getUndoManager().undo());
        redoItem.addActionListener(e -> model.getUndoManager().redo());
        cutItem.addActionListener(e -> textArea.cut());
        copyItem.addActionListener(e -> textArea.copy());
        pasteItem.addActionListener(e -> textArea.paste());
        findReplaceItem.addActionListener(e -> findReplaceDialog.setVisible(true));
        fontItem.addActionListener(e -> fontController.chooseFont());
        themeToggle.addActionListener(e -> themeController.toggleTheme());
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        findReplaceItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(formatMenu);
        menuBar.add(viewMenu);
        return menuBar;
    }

    private void setupListeners() {
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { model.setContent(textArea.getText()); }
            @Override public void removeUpdate(DocumentEvent e) { model.setContent(textArea.getText()); }
            @Override public void changedUpdate(DocumentEvent e) { model.setContent(textArea.getText()); }
        });
        textArea.addCaretListener(e -> {
            int caretPos = textArea.getCaretPosition();
            int line = 1, column = 1;
            try { line = textArea.getLineOfOffset(caretPos) + 1; column = caretPos - textArea.getLineStartOffset(line - 1) + 1; }
            catch (Exception ex) {}
            statusBar.updatePosition(line, column);
        });
        textArea.getDocument().addUndoableEditListener(e -> model.getUndoManager().addEdit(e.getEdit()));
    }
    
    public void updateRecentFilesMenu() {
        recentFilesMenu.removeAll();
        List<String> recentFiles = recentFilesManager.getRecentFiles();
        if (recentFiles.isEmpty()) { recentFilesMenu.setEnabled(false); }
        else {
            recentFilesMenu.setEnabled(true);
            for (String filePath : recentFiles) {
                JMenuItem item = new JMenuItem(filePath);
                item.addActionListener(e -> fileController.openFile(Path.of(filePath)));
                recentFilesMenu.add(item);
            }
        }
    }

    public void updateTitle() {
        String title = "Profesyonel Not Defteri";
        if (model.getCurrentFilePath() != null) title = model.getCurrentFilePath().getFileName().toString() + " - " + title;
        if (model.isDirty()) title = "*" + title;
        setTitle(title);
    }
    
    public JTextArea getTextArea() { return textArea; }
}