package au.com.refactor

import com.codeminders.hidapi.HIDDevice
import com.codeminders.hidapi.HIDManager
import org.junit.Rule
import org.junit.Test
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.github.tomakehurst.wiremock.junit.WireMockRule

import static junit.framework.TestCase.assertFalse
import static junit.framework.TestCase.fail
import static org.junit.Assert.assertTrue

class BuildWatcherTest {

    // TODO: Currently needs the build light plugged in to work. Need to mock the hardware out.
    // TODO: Need to add auth to wiremock

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089); // No-args constructor defaults to port 8080

    @Test
    void greenBuild() {

        String xml = '''<Projects>
<Project webUrl="http://localhost:8089/job/firstapp/" name="firstapp" lastBuildLabel="11" lastBuildTime="2014-09-16T23:28:32Z" lastBuildStatus="Success" activity="Sleeping"/>
<Project webUrl="http://localhost:8089/job/secondapp/" name="secondapp" lastBuildLabel="1" lastBuildTime="2014-10-07T12:30:20Z" lastBuildStatus="Success" activity="Sleeping"/>
</Projects>'''

        stubFor(get(urlEqualTo("/cc.xml"))
                .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "text/xml")
                .withBody(xml)))

        BuildWatcher bw = new BuildWatcher('localhost', 8089, 'user', 'token')

        bw.checkBuildAndSignal({}, {fail("failed method incorrectly called")}, {fail("'unknown' method incorrectly called")})
    }

    @Test
    void redBuild() {

        String xml = '''<Projects>
<Project webUrl="http://localhost:8089/job/firstapp/" name="firstapp" lastBuildLabel="11" lastBuildTime="2014-09-16T23:28:32Z" lastBuildStatus="Success" activity="Sleeping"/>
<Project webUrl="http://localhost:8089/job/secondapp/" name="secondapp" lastBuildLabel="1" lastBuildTime="2014-10-07T12:30:20Z" lastBuildStatus="Failure" activity="Sleeping"/>
</Projects>'''

        stubFor(get(urlEqualTo("/cc.xml"))
                .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "text/xml")
                .withBody(xml)))

        BuildWatcher bw = new BuildWatcher('localhost', 8089, 'user', 'token')

        bw.checkBuildAndSignal({fail("success method incorrectly called")}, {}, {fail("'unknown' method incorrectly called")})
    }

    @Test
    void cantConnectToJenkins() {

        stubFor(get(urlEqualTo("/cc.xml"))
                .withHeader("Accept", equalTo("text/xml"))
                .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "text/xml")
                .withBody("")))

        BuildWatcher bw = new BuildWatcher('localhost', 8089, 'user', 'token')

        bw.checkBuildAndSignal({fail("success method incorrectly called")}, {fail("fail method incorrectly called")}, {})
    }


}
