package model;

import java.util.prefs.Preferences;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecentFilesManager {
    private static final int MAX_RECENT_FILES = 5;
    private static final String RECENT_FILES_KEY = "RecentFiles";
    private final Preferences prefs;

    public RecentFilesManager() {
        prefs = Preferences.userNodeForPackage(RecentFilesManager.class);
    }

    public void addRecentFile(Path path) {
        if (path == null) return;
        List<String> recentFiles = getRecentFiles();
        recentFiles.remove(path.toString());
        recentFiles.add(0, path.toString());
        if (recentFiles.size() > MAX_RECENT_FILES) {
            recentFiles = recentFiles.subList(0, MAX_RECENT_FILES);
        }
        prefs.put(RECENT_FILES_KEY, String.join(";", recentFiles));
    }

    public List<String> getRecentFiles() {
        String recentFilesString = prefs.get(RECENT_FILES_KEY, "");
        if (recentFilesString.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(List.of(recentFilesString.split(";")));
    }
}