package au.com.refactor

import com.codeminders.hidapi.HIDDevice
import com.codeminders.hidapi.HIDManager

class BuildWatcher {

    public static void main(String[] args) {
        HIDManager hidManager = HIDManager.getInstance();
        HIDDevice dev = hidManager.openById(0x0fc5, 0xb08, null)
    }
}