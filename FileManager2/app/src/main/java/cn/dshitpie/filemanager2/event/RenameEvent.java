package cn.dshitpie.filemanager2.event;

import java.io.File;

public class RenameEvent {
    private File file;

    public RenameEvent(File file) {
        this.file = file;
    }

    public File tellRenamedFile() {
        return file;
    }
}
