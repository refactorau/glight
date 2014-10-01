package au.com.refactor

import com.codeminders.hidapi.ClassPathLibraryLoader
import com.codeminders.hidapi.HIDDevice
import com.codeminders.hidapi.HIDManager

class BuildWatcher {

    private final byte[] SET_STRUCTURE = [
        (byte) 0x65,
        (byte) 0x02,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00,
        (byte) 0x00
    ]

    private final int SET_BYTE = 2;
    private Colour currentColour = Colour.BLACK
    private HIDDevice device

    public static void main(String[] args) {
        new BuildWatcher()
    }

    BuildWatcher() {
        ClassPathLibraryLoader.loadNativeHIDLibrary()
        HIDManager hidManager = HIDManager.getInstance();
//        println hidManager.listDevices()
        device = hidManager.openById(4037, 45184, null)

        red()
        sleep(1000)
        green()
        sleep(1000)
        off()
    }

    void setColour(Colour colour) {
        byte[] buffer = SET_STRUCTURE.clone();
        buffer[SET_BYTE] = colour.getCode();
        device.sendFeatureReport(buffer);
        currentColour = colour;
    }

    void red() {
        setColour(Colour.RED)
    }

    void green() {
        setColour(Colour.GREEN)
    }

    void off() {
        setColour(Colour.BLACK)
    }
}