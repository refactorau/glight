package au.com.refactor

import org.apache.commons.httpclient.*

import org.apache.commons.httpclient.auth.*
import org.apache.commons.httpclient.methods.*

import com.codeminders.hidapi.ClassPathLibraryLoader
import com.codeminders.hidapi.HIDDevice
import com.codeminders.hidapi.HIDManager

import javax.swing.plaf.ButtonUI

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
    private String server, url, user, token
    private int port
    private HttpClient client


    public static void main(String[] args) {



        String server = System.properties['jenkins.server']
        String port = System.properties['jenkins.port']
        String user = System.properties['jenkins.user']
        String token = System.properties['jenkins.api.token']

        BuildWatcher bw = new BuildWatcher(server, Integer.parseInt(port), user, token)

        bw.watch()

    }

    BuildWatcher(String server, int port, String user, String token) {

        this.server = server
        this.port = port
        this.user = user
        this.token = token
        this.url = "http://${server}:${port}/cc.xml"

        ClassPathLibraryLoader.loadNativeHIDLibrary()
        HIDManager hidManager = HIDManager.getInstance();
//        println hidManager.listDevices()
        device = hidManager.openById(4037, 45184, null)
        client = createClient()
    }

    void setColour(Colour colour) {
        byte[] buffer = SET_STRUCTURE.clone();
        buffer[SET_BYTE] = colour.getCode();
        device.sendFeatureReport(buffer);
        currentColour = colour;
    }

    void red() {
        println('Red')
        setColour(Colour.RED)
    }

    void green() {
        println('Green')
        setColour(Colour.GREEN)
    }

    void blue() {
        println('Blue')
        setColour(Colour.BLUE)
    }

    void off() {
        println('Off')
        setColour(Colour.BLACK)
    }

    void demo() {
        while (true) {
            [Colour.RED, Colour.BLUE, Colour.GREEN].each {
                setColour(it)
                sleep(1000)
            }
        }
    }

    void watch() {
        while (true) {
            println("Checking...")
            checkBuildAndSignal(this.&green, this.&red, this.&blue)
            sleep(30000)
        }
    }

    private HttpClient createClient() {
        // only do this if client not initialised
        def client = new HttpClient()
        client.state.setCredentials(
                new AuthScope(server, port, "realm"),
                new UsernamePasswordCredentials(user, token)
        )
        client.params.authenticationPreemptive = true
        return client
    }

    void checkBuildAndSignal(Closure success, Closure fail, Closure unknown) {

        def get = new GetMethod(url)
        get.doAuthentication = true

        try {
            int result = client.executeMethod(get)

            if (result == 200) {
                if (isBuildSuccess(get.getResponseBodyAsString())) {
                    success()
                } else {
                    fail()
                }
            } else {
                unknown()
            }
        }
        catch(UnknownHostException uhe) {
            uhe.printStackTrace()
            unknown()
        }
    }

    static boolean isBuildSuccess(String xml) {
        def projects = new XmlSlurper().parseText(xml)
        def failCount = projects.Project.findAll { it.@lastBuildStatus == 'Failure' }.size()
        return failCount == 0
    }
}