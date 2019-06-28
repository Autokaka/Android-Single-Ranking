package cn.dshitpie.filemanager2.event;

public class NewBuildEvent {
    private int result;

    public NewBuildEvent(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}
