package h4dro.me.catusrtp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {

    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("&#([0-9a-fA-F]{6})");
    private static final Pattern AMPERSAND_COLOR_PATTERN = Pattern.compile("&([0-9a-fA-FK-OR])");

    public static String translateColors(String text) {
        text = translateHexColors(text);
        text = translateAmpersandColors(text);
        return text;
    }

    private static String translateHexColors(String text) {
        Matcher matcher = HEX_COLOR_PATTERN.matcher(text);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String hexColor = matcher.group(1);
            String minecraftColor = hexToMinecraftColor(hexColor);
            matcher.appendReplacement(sb, minecraftColor);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String hexToMinecraftColor(String hex) {
        return "§x§" + hex.charAt(0) + "§" + hex.charAt(1) + "§" + hex.charAt(2) + "§" + hex.charAt(3) + "§" + hex.charAt(4) + "§" + hex.charAt(5);
    }

    private static String translateAmpersandColors(String text) {
        Matcher matcher = AMPERSAND_COLOR_PATTERN.matcher(text);
        return matcher.replaceAll("§$1");
    }
}