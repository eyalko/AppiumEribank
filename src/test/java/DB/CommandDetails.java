package DB;


public class CommandDetails {
    public String when, command;
    public long time;
    public Object toLog;
    public String xpath;

    public CommandDetails(String when, String command, long time, Object toLog, String xpath) {
        this.when = when;
        this.command = command;
        this.time = time;
        this.toLog = toLog;
        this.xpath = xpath;
    }

    @Override
    public String toString() {
        return "CommandDetails{" +
                "when='" + when + '\'' +
                ", command='" + command + '\'' +
                ", time=" + time +
                ", toLog=" + toLog +
                ", xpath='" + xpath + '\'' +
                '}';
    }
}
