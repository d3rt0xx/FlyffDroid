package app.d3rt0xx.flyffdroid;

public enum Websites {

    DISCORD("https://discord.gg/flyffuniverse"),
    EXPTABLE("https://www.flyff.me"),
    FACEBOOK("https://www.facebook.com/uflyff"),
    FLYFFIPEDIA("https://flyffipedia.com"),
    FLYFFULATOR("https://flyffulator.com"),
    GAME("https://universe.flyff.com/play"),
    GUILDULATOR("https://guildulator.vercel.app"),
    MADRIGALMAPS("https://www.madrigalmaps.com"),
    MODELVIEWER("https://flyffmodelviewer.com"),
    PATCHNOTES("https://universe.flyff.com/news"),
    SUPPORT("https://galalab.helpshift.com/a/flyff-universe"),
    UPDATE("https://github.com/d3rt0xx/FlyffDroid/releases/latest");

    private final String url;

    Websites(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
