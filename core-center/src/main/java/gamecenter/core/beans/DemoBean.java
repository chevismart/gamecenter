package gamecenter.core.beans;

/**
 * Created by Chevis on 2014/12/8.
 */
public class DemoBean {

    private String name;

    @Override
    public String toString() {
        return "DemoBean{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
