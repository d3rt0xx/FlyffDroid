package app.d3rt0xx.flyffdroid;

public enum Websites {

    DISCORD("https://discord.gg/flyffuniverse"),
    GAME("https://universe.flyff.com/play"),
    FLYFFDROID("https://github.com/d3rt0xx/FlyffDroid"),
    FLYFFIPEDIA("https://flyffipedia.com"),
    FLYFFULATOR("https://flyffulator.com"),
    GUILDULATOR("https://guildulator.vercel.app"),
    MADRIGALMAPS("https://www.madrigalmaps.com"),
    MARKETPLACE("https://flyffutools.com/marketplace"),
    MODELVIEWER("https://flyffmodelviewer.com"),
    PARTNERFINDER("https://flyffuinfo.com/partner-finder"),
    PATCHNOTES("https://universe.flyff.com/news"),
    SKILLULATOR("https://skillulator.com");

    private final String url;

    Websites(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
