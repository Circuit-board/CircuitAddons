package cool.circuit.circuitAddons.vault;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public class CircuitChat extends Chat {
    public CircuitChat(Permission perms) {
        super(perms);
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getPlayerPrefix(String s, String s1) {
        return "";
    }

    @Override
    public void setPlayerPrefix(String s, String s1, String s2) {

    }

    @Override
    public String getPlayerSuffix(String s, String s1) {
        return "";
    }

    @Override
    public void setPlayerSuffix(String s, String s1, String s2) {

    }

    @Override
    public String getGroupPrefix(String s, String s1) {
        return "";
    }

    @Override
    public void setGroupPrefix(String s, String s1, String s2) {

    }

    @Override
    public String getGroupSuffix(String s, String s1) {
        return "";
    }

    @Override
    public void setGroupSuffix(String s, String s1, String s2) {

    }

    @Override
    public int getPlayerInfoInteger(String s, String s1, String s2, int i) {
        return 0;
    }

    @Override
    public void setPlayerInfoInteger(String s, String s1, String s2, int i) {

    }

    @Override
    public int getGroupInfoInteger(String s, String s1, String s2, int i) {
        return 0;
    }

    @Override
    public void setGroupInfoInteger(String s, String s1, String s2, int i) {

    }

    @Override
    public double getPlayerInfoDouble(String s, String s1, String s2, double v) {
        return 0;
    }

    @Override
    public void setPlayerInfoDouble(String s, String s1, String s2, double v) {

    }

    @Override
    public double getGroupInfoDouble(String s, String s1, String s2, double v) {
        return 0;
    }

    @Override
    public void setGroupInfoDouble(String s, String s1, String s2, double v) {

    }

    @Override
    public boolean getPlayerInfoBoolean(String s, String s1, String s2, boolean b) {
        return false;
    }

    @Override
    public void setPlayerInfoBoolean(String s, String s1, String s2, boolean b) {

    }

    @Override
    public boolean getGroupInfoBoolean(String s, String s1, String s2, boolean b) {
        return false;
    }

    @Override
    public void setGroupInfoBoolean(String s, String s1, String s2, boolean b) {

    }

    @Override
    public String getPlayerInfoString(String s, String s1, String s2, String s3) {
        return "";
    }

    @Override
    public void setPlayerInfoString(String s, String s1, String s2, String s3) {

    }

    @Override
    public String getGroupInfoString(String s, String s1, String s2, String s3) {
        return "";
    }

    @Override
    public void setGroupInfoString(String s, String s1, String s2, String s3) {

    }
}
