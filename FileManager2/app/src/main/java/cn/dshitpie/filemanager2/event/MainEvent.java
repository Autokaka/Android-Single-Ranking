package cn.dshitpie.filemanager2.event;

import java.io.File;

/**
 * EventBus自定义消息处理类
 * */
public class MainEvent {
    private File file;

    /**
     * 当Subscribers订阅了这个类的时候, 它可以从这个类的自定义Onxxxxx(随便写名字, 你开心就好)回调接口中取出activity/Main发出的消息
     * */
    public MainEvent(File file) {
        this.file = file;
    }

    /**
     * 取出activity/Main事先设置好的消息. InFile:当前所在目录
     * */
    public File tellInFile() {
        return file;
    }

    /**
     * SelectFile:当前RecyclerView选中的item对应的目录
     * */
    public File tellSelectFile() {
        return file;
    }
}
