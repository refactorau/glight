package au.com.refactor

import com.codeminders.hidapi.ClassPathLibraryLoader
import com.codeminders.hidapi.HIDDevice
import com.codeminders.hidapi.HIDManager

class BuildWatcher {


    public static void main(String[] args) {
        new BuildWatcher()
    }

    BuildWatcher() {
        ClassPathLibraryLoader.loadNativeHIDLibrary()
        HIDManager hidManager = HIDManager.getInstance();
//        println hidManager.listDevices()
        HIDDevice dev = hidManager.openById(4037, 45184, null)
    }

    void red() {

    }

    void green() {

    }

    void off() {

    }
}