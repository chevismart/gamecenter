package gamecenter.core.processors.tasks;

import gamecenter.core.processors.wechat.ProfileManager;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class AppLoaderTest {

    @Test
    public void loaderSuccessullyLoadedAppProfiles() throws Exception {

        ProfileManager mockProfileManager = mock(ProfileManager.class);
        AppLoader appLoader = new AppLoader(mockProfileManager);
        appLoader.run();
    }
}