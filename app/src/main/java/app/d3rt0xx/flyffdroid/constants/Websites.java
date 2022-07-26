package app.d3rt0xx.flyffdroid.constants;

public enum Websites {

    GAME("https://universe.flyff.com/play"),
    NEWS("https://universe.flyff.com/news"),
    MARKETPLACE("https://flyffutools.com/marketplace"),
    FLYFFIPEDIA("https://flyffipedia.com"),
    FLYFFULATOR("https://flyffulator.com"),
    SKILLULATOR("https://skillulator.com"),
    MADRIGALMAPS("https://www.madrigalmaps.com"),
    FLYFFDROID("https://github.com/d3rt0xx/FlyffDroid");

    private final String url;

    Websites(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
