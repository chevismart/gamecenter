package gamecenter.core.processors.tasks;

import gamecenter.core.processors.wechat.ProfileManager;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class AppLoaderTest {

    AppLoader appLoader = new AppLoader();

    @Test
    public void loaderSuccessullyLoadedAppProfiles() throws Exception {

        ProfileManager mockProfileManager = mock(ProfileManager.class);
        appLoader.setProfileManager(mockProfileManager);

        appLoader.run();

    }
}