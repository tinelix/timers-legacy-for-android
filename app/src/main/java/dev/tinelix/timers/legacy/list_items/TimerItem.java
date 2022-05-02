package dev.tinelix.timers.legacy.list_items;

public class TimerItem {
    public String name;
    public String action;
    public long actionDate;
    public String iconName;
    public TimerItem(String substring, String _name, String _action, long _actionDate, String _iconName) {
        name = _name;
        action = _action;
        actionDate = _actionDate;
        iconName = _iconName;
    }
}
